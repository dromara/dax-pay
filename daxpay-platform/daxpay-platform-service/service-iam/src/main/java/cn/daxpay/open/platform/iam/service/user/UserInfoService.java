package cn.daxpay.open.platform.iam.service.user;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.convert.user.UserConvert;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.service.session.OnlineUserService;
import cn.daxpay.open.platform.iam.param.user.UserBaseInfoParam;
import cn.daxpay.open.platform.iam.result.user.LoginAfterUserInfoResult;
import cn.daxpay.open.platform.iam.result.user.PasswordStatusResult;
import cn.daxpay.open.platform.iam.result.user.UserBaseInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 用户
///
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoManager userInfoManager;

    private final UserExpandInfoManager userExpandInfoManager;

    private final PasswordPolicyService passwordPolicyService;

    private final UserPasswordSecurityManager passwordSecurityManager;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final LoginRetryService loginRetryService;

    private final PasswordDecryptService passwordDecryptService;

    private final UserQueryService userQueryService;

    private final OnlineUserService onlineUserService;

    /// 登录后获取用户信息
    public LoginAfterUserInfoResult getLoginAfterUserInfo() {
        Long userId = SecurityUtil.getUserId();
        UserInfo userInfo = userInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        // 超级管理员按既定策略绕过强制改密，避免前端仍根据数据库状态强制跳转。
        PasswordStatusResult passwordStatus = userInfo.isAdministrator()
                ? new PasswordStatusResult()
                .setExpired(false)
                .setExpiringSoon(false)
                .setExpireTime(null)
                .setInitialPassword(false)
                : loginRetryService.getPasswordStatus(userId);
        return new LoginAfterUserInfoResult()
            .setId(userInfo.getId())
            .setAccount(userInfo.getAccount())
            .setName(userInfo.getName())
            .setAvatar(userExpandInfo.getAvatar())
            .setPasswordStatus(passwordStatus);
    }

    /// 获取用户基本信息
    public UserBaseInfoResult getUserBaseInfo() {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        return new UserBaseInfoResult().setId(userInfo.getId())
            .setSex(userExpandInfo.getSex())
            .setName(userInfo.getName())
            .setBirthday(userExpandInfo.getBirthday())
            .setAvatar(userExpandInfo.getAvatar())
            .setEmail(userInfo.getEmail());
    }

    /// 修改基本信息
    ///
    /// email 与手机号均不在本接口受理:
    /// email 是找回密码的安全凭证, 变更仅允许走 /user/auth/email 绑定验证流程;
    /// 手机号功能已冻结, 待接入短信验证后启用(参数中已无对应字段, copy 自然不映射)
    @Transactional(rollbackFor = Exception.class)
    public void updateUserBaseInfo(UserBaseInfoParam param) {
        Long userId = SecurityUtil.getUserId();
        UserInfo userInfo = userInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        UserConvert.CONVERT.copy(param, userExpandInfo);
        UserConvert.CONVERT.copy(param, userInfo);
        userExpandInfoManager.updateById(userExpandInfo);
        userInfoManager.updateById(userInfo);
    }

    /// 修改密码
    /// @param password 原密码（加密传输）
    /// @param newPassword 新密码（加密传输）
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password, String newPassword) {
        // 解密密码
        String decryptedPassword = passwordDecryptService.decryptPassword(password);
        String decryptedNewPassword = passwordDecryptService.decryptPassword(newPassword);

        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        // 前置: 账号处于锁定状态时拒绝尝试(与登录锁定共用状态)
        loginRetryService.checkBeforeSensitiveVerify(userInfo.getId());
        // 判断原密码是否正确
        if (!BCrypt.checkpw(decryptedPassword, userInfo.getPassword())) {
            // 失败计数(REQUIRES_NEW 独立事务, 不随本方法事务回滚)
            loginRetryService.onSensitiveVerifyFailure(userInfo.getId(), userInfo.getAccount());
            // 权限: 旧密码错误
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.oldPasswordError");
        }
        // 验证通过, 清零失败计数(与登录成功同口径)
        loginRetryService.onSensitiveVerifySuccess(userInfo.getId());
        // 验证密码历史
        passwordPolicyService.validatePasswordHistory(userInfo.getId(), decryptedNewPassword);
        passwordPolicyService.validatePassword(decryptedNewPassword);
        String passwordHash = BCrypt.hashpw(decryptedNewPassword, BCrypt.gensalt());
        UserInfo update = new UserInfo();
        update.setId(userInfo.getId());
        update.setVersion(userInfo.getVersion());
        update.setPassword(passwordHash);
        userInfoManager.updateById(update);
        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);
        // 更新密码过期时间和初始密码标记
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        passwordSecurityManager.updatePasswordExpireTime(userInfo.getId(), passwordExpireTime);
        // 修改密码成功后, 保留当前会话, 强制该用户其他所有会话下线
        // 注册事务提交后回调, 避免事务回滚时误踢下线
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 先刷新当前会话，避免改密后权限请求仍读取旧的初始密码状态
                refreshCurrentSessionPasswordStatus();
                onlineUserService.kickoutOtherSessions(userInfo.getId());
            }
        });
    }

    /// 刷新当前会话中的密码状态
    private void refreshCurrentSessionPasswordStatus() {
        var session = StpUtil.getSession();
        UserDetail userDetail = session.getModel(CommonCode.USER, UserDetail.class);
        if (userDetail == null) {
            return;
        }
        if (userDetail.isAdmin()) {
            userDetail.setPasswordExpired(false)
                    .setInitialPassword(false)
                    .setPasswordExpireTime(null);
        }
        else {
            loginRetryService.setPasswordStatusToUserDetail(userDetail);
        }
        session.set(CommonCode.USER, userDetail);
    }

    /// 计算密码过期时间 (UTC)
    private OffsetDateTime calculatePasswordExpireTime() {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy();
        Integer rotationDays = config.getRotationDays();
        if (rotationDays == null || rotationDays <= 0) {
            return null;
        }
        return OffsetDateTime.now(ZoneOffset.UTC).plusDays(rotationDays);
    }

}

