package cn.daxpay.open.platform.iam.service.user;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.convert.user.UserConvert;
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
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import cn.daxpay.open.platform.core.code.CommonCode;

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
        PasswordStatusResult passwordStatus = loginRetryService.getPasswordStatus(userId);
        return new LoginAfterUserInfoResult()
            .setId(userInfo.getId())
            .setAccount(userInfo.getAccount())
            .setName(userInfo.getName())
            .setAvatar(userExpandInfo.getAvatar())
            .setPasswordStatus(passwordStatus);
    }

    /// 获取用户安全信息
    public UserInfoResult getUserSecurityInfo() {
        return userInfoManager.findById(SecurityUtil.getUserId())
            .map(UserInfo::toResult)
            .orElseThrow(UserInfoNotExistsException::new);
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
            .setEmail(userInfo.getEmail())
            .setPhone(userInfo.getPhone());
    }

    /// 修改基本信息
    @Transactional(rollbackFor = Exception.class)
    public void updateUserBaseInfo(UserBaseInfoParam param) {
        Long userId = SecurityUtil.getUserId();
        UserInfo userInfo = userInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId)
            .orElseThrow(UserInfoNotExistsException::new);
        // 邮箱唯一性校验（排除自身）
        if (userQueryService.existsEmail(param.getEmail(), userId)) {
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedByOther");
        }
        // 手机号唯一性校验（排除自身）
        if (userQueryService.existsPhone(param.getPhone(), userId)) {
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.phoneUsedByOther");
        }
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
        // 判断原密码是否正确
        if (!BCrypt.checkpw(decryptedPassword, userInfo.getPassword())) {
            // 权限: 旧密码错误
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.oldPasswordError");
        }
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
                onlineUserService.kickoutOtherSessions(userInfo.getId());
            }
        });
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

