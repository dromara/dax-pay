package cn.daxpay.open.platform.iam.service.user;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.convert.user.UserConvert;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.param.user.UserInfoQuery;
import cn.daxpay.open.platform.iam.result.user.UserWholeInfoResult;
import cn.daxpay.open.platform.iam.service.session.OnlineUserService;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 超级管理员操作类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserInfoManager userInfoManager;

    private final UserExpandInfoManager userExpandInfoManager;

    private final UserQueryService userQueryService;

    private final PlatformStarterProperties platformStarterProperties;

    private final PasswordPolicyService passwordPolicyService;

    private final UserPasswordSecurityManager passwordSecurityManager;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final PasswordDecryptService passwordDecryptService;

    private final OnlineUserService onlineUserService;

    /// 分页查询（按终端过滤）
    public PageResult<UserWholeInfoResult> page(PageParam pageParam, UserInfoQuery query) {

        Page<UserWholeInfoResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        boolean showAdminInList = platformStarterProperties.getAuth().isEnableAdmin()
                && platformStarterProperties.getAuth().isAdminInList()
                && Objects.equals(ClientEnum.ADMIN.getCode(), query.getClientCode());
        wrapper.innerJoin(UserExpandInfo.class,UserExpandInfo::getId, UserInfo::getId)
                .selectAll(UserInfo.class)
                .selectAll(UserExpandInfo.class)
                // 仅 ADMIN 端在内置超管能力开启后，才允许在列表中展示超管账号
                .eq(!showAdminInList, UserInfo::isAdministrator, false)
                // 按终端过滤
                .eq(StrUtil.isNotBlank(query.getClientCode()), UserInfo::getClientCode, query.getClientCode())
                // 按账号状态过滤
                .eq(StrUtil.isNotBlank(query.getStatus()), UserInfo::getStatus, query.getStatus())
                // 按名称模糊查询
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName())

                // 按账号模糊查询
                .like(StrUtil.isNotBlank(query.getAccount()), UserInfo::getAccount, query.getAccount())
                // 按手机号模糊查询
                .like(StrUtil.isNotBlank(query.getPhone()), UserInfo::getPhone, query.getPhone())
                // 按邮箱模糊查询
                .like(StrUtil.isNotBlank(query.getEmail()), UserInfo::getEmail, query.getEmail());
        Page<UserWholeInfoResult> page = userInfoManager.selectJoinListPage(mpPage, UserWholeInfoResult.class, wrapper);
        return new PageResult<UserWholeInfoResult>()
                .setRecords(page.getRecords())
                .setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setTotal(page.getTotal());
    }

    /// 封禁用户
    public void ban(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusEnum.BAN.getCode());
    }

    /// 批量封禁用户
    public void banBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusEnum.BAN.getCode());
    }

    /// 锁定用户
    public void lock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusEnum.LOCK.getCode());
    }

    /// 批量锁定用户
    public void lockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusEnum.LOCK.getCode());
    }

    /// 解锁用户
    public void unlock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusEnum.NORMAL.getCode());
    }

    /// 批量解锁用户
    public void unlockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusEnum.NORMAL.getCode());
    }

    /// 添加新用户（终端维度唯一性校验）
    @Transactional(rollbackFor = Exception.class)
    public UserInfo add(UserInfoParam userInfoParam) {
        return add(userInfoParam, false);
    }

    /// 添加新用户
    /// @param skipDuplicateCheck 是否跳过重复校验（调用方已做更精确的校验时使用）
    @Transactional(rollbackFor = Exception.class)
    public UserInfo add(UserInfoParam userInfoParam, boolean skipDuplicateCheck) {
        // 使用传入的终端编码，未指定时默认为 admin
        String clientCode = StrUtil.isNotBlank(userInfoParam.getClientCode())
                ? userInfoParam.getClientCode()
                : ClientEnum.ADMIN.getCode();
        userInfoParam.setClientCode(clientCode);
        if (!skipDuplicateCheck) {
            // 按终端校验账号唯一性
            if (userQueryService.existsAccountByClientCode(clientCode, userInfoParam.getAccount())) {
                // 权限: 该终端下账号已存在
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.accountExistsInClient");
            }
            // 按终端校验手机号唯一性
            if (userQueryService.existsPhoneByClientCode(clientCode, userInfoParam.getPhone())) {
                // 权限: 该终端下手机号已被使用
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.phoneUsedInClient");
            }
            // 按终端校验邮箱唯一性
            if (userQueryService.existsEmailByClientCode(clientCode, userInfoParam.getEmail())) {
                // 权限: 该终端下邮箱已被使用
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedInClient");
            }
        }
        // 解密密码
        String password = passwordDecryptService.decryptPassword(userInfoParam.getPassword());
        passwordPolicyService.validatePassword(password);
        UserInfo userInfo = UserInfo.init(userInfoParam);
        userInfo.setAdministrator(false)
                .setStatus(UserStatusEnum.NORMAL.getCode());
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.save(userInfo);
        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);
        // 初始化密码安全信息（设置初始密码标记和密码过期时间）
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        passwordSecurityManager.initPasswordSecurity(userInfo.getId(), passwordExpireTime);
        // 扩展信息
        UserExpandInfo userExpandInfo = new UserExpandInfo()
                .setRegisterTime(OffsetDateTime.now(ZoneOffset.UTC));
        userExpandInfo.setId(userInfo.getId());
        userExpandInfoManager.save(userExpandInfo);
        return userInfo;
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

    /// 重置密码
    /// @param userId 用户ID
    /// @param newPassword 新密码（加密传输）
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(Long userId, String newPassword) {
        // 解密密码
        String decryptedPassword = passwordDecryptService.decryptPassword(newPassword);
        // 验证密码历史
        passwordPolicyService.validatePasswordHistory(userId, decryptedPassword);
        passwordPolicyService.validatePassword(decryptedPassword);

        UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
        // 新密码进行加密
        String passwordHash = BCrypt.hashpw(decryptedPassword, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.updateById(userInfo);
        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userId, passwordHash);
        // 更新密码过期时间, 重置后的密码视为初始密码(强制用户首次登录自行改密)
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        passwordSecurityManager.updatePasswordExpireTimeOnReset(userId, passwordExpireTime);
        // 重置密码成功后, 强制该用户全部会话下线(管理员操作, 不保留任何会话)
        // 注册事务提交后回调, 避免事务回滚时误踢下线
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                onlineUserService.kickoutAllSessions(userId);
            }
        });
    }

    /// 批量重置密码
    /// @param userIds 用户ID列表
    /// @param newPassword 新密码（加密传输）
    @Transactional(rollbackFor = Exception.class)
    public void restartPasswordBatch(List<Long> userIds, String newPassword){
        // 解密密码
        String decryptedPassword = passwordDecryptService.decryptPassword(newPassword);
        passwordPolicyService.validatePassword(decryptedPassword);
        String passwordHash = BCrypt.hashpw(decryptedPassword, BCrypt.gensalt());
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        // 为每个用户验证密码历史并保存历史记录
        for (Long userId : userIds) {
            passwordPolicyService.validatePasswordHistory(userId, decryptedPassword);
            passwordPolicyService.savePasswordHistory(userId, passwordHash);
            // 更新密码过期时间, 重置后的密码视为初始密码(强制用户首次登录自行改密)
            passwordSecurityManager.updatePasswordExpireTimeOnReset(userId, passwordExpireTime);
        }
        userInfoManager.restartPasswordBatch(userIds, passwordHash);
        // 批量重置密码成功后, 强制每个目标用户全部会话下线
        // 注册事务提交后回调, 避免事务回滚时误踢下线
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (Long userId : userIds) {
                    onlineUserService.kickoutAllSessions(userId);
                }
            }
        });
    }

    /// 编辑用户信息（禁止修改终端归属）
    /// 只允许编辑运营端(admin)用户
    public void update(UserInfoParam userInfoParam) {
        UserInfo userInfo = userInfoManager.findById(userInfoParam.getId())
                .orElseThrow(UserInfoNotExistsException::new);
        // 禁止编辑非运营端用户
        if (!ClientEnum.ADMIN.getCode().equals(userInfo.getClientCode())) {
            // 权限: 不能编辑非运营端用户
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.cannotEditNonAdmin");
        }
        // 禁止修改终端归属
        userInfoParam.setClientCode(userInfo.getClientCode());
        // 按终端校验手机号唯一性（排除自身）
        if (userQueryService.existsPhoneByClientCode(userInfo.getClientCode(), userInfoParam.getPhone(), userInfoParam.getId())) {
            // 权限: 该终端下手机号已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.phoneUsedByOtherInClient");
        }
        // 按终端校验邮箱唯一性（排除自身）
        if (userQueryService.existsEmailByClientCode(userInfo.getClientCode(), userInfoParam.getEmail(), userInfoParam.getId())) {
            // 权限: 该终端下邮箱已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedByOtherInClient");
        }
        userInfoParam.setPassword(null);
        UserConvert.CONVERT.copy(userInfoParam, userInfo);
        userInfoManager.updateById(userInfo);
    }
}

