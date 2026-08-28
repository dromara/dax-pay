package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.payment.merchant.convert.info.MerchantUserConvert;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/// # 商户用户管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserAdminService {

    private final UserInfoManager userInfoManager;
    private final UserExpandInfoManager userExpandInfoManager;
    private final MerchantUserManager merchantUserManager;
    private final MerchantInfoManager merchantInfoManager;
    private final PasswordDecryptService passwordDecryptService;
    private final PasswordPolicyService passwordPolicyService;
    private final UserPasswordSecurityManager passwordSecurityManager;
    private final UserRoleService userRoleService;
    private final UserQueryService userQueryService;
    private final UserAdminService userAdminService;
    private final IamSecurityConfigService iamSecurityConfigService;

    /// 分页查询商户用户
    public PageResult<MerchantUserResult> page(PageParam pageParam, MerchantUserQuery query) {
        Page<MerchantUserResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .innerJoin(MerchantInfo.class, MerchantInfo::getMchNo, MerchantUser::getMchNo)
                .selectAll(UserInfo.class)
                .select(MerchantUser::isAdministrator)
                .select(MerchantInfo::getMchNo)
                .select(MerchantInfo::getMchName)
                .eq(StrUtil.isNotBlank(query.getMchNo()), MerchantInfo::getMchNo, query.getMchNo())
                .eq(StrUtil.isNotBlank(query.getStatus()), UserInfo::getStatus, query.getStatus())
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getAccount()), UserInfo::getAccount, query.getAccount());
        Page<MerchantUserResult> page = userInfoManager.selectJoinListPage(mpPage, MerchantUserResult.class, wrapper);
        return MpUtil.toPageResult(page);
    }

    /// 根据用户ID查询用户详情
    public UserInfoResult findById(Long id) {
        this.checkMerchantUser(id);
        return userInfoManager.findById(id)
                .map(UserInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /// 添加商户用户
    /// @return 含初始密码明文的结果(未传密码时由系统生成随机密码)
    @Transactional(rollbackFor = Exception.class)
    public UserPasswordResult add(MerchantUserParam param) {
        String mchNo = param.getMchNo();
        MerchantInfo merchantInfo = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.merchantNotExist"));

        // 校验账号唯一性（商户终端）
        String clientCode = ClientEnum.MERCHANT.getCode();
        if (userQueryService.existsAccountByClientCode(clientCode, param.getAccount())) {
            // 商户: 该账号已存在
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.accountExists");
        }

        // 密码可选: 未传时生成随机密码, 传入时按 RSA 密文解密(兼容存量调用方)
        String password = StrUtil.isBlank(param.getPassword())
                ? passwordPolicyService.generateSecurePassword()
                : passwordDecryptService.decryptPassword(param.getPassword());
        passwordPolicyService.validatePassword(password);

        // 创建用户
        UserInfo userInfo = MerchantUserConvert.CONVERT.toEntity(param);
        userInfo.setClientCode(clientCode)
                .setAdministrator(false)
                .setStatus(UserStatusEnum.NORMAL.getCode());
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.save(userInfo);

        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);

        // 创建用户扩展信息
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        passwordSecurityManager.initPasswordSecurity(userInfo.getId(), passwordExpireTime);

        UserExpandInfo userExpandInfo = new UserExpandInfo()
                .setRegisterTime(OffsetDateTime.now(ZoneOffset.UTC));
        userExpandInfo.setId(userInfo.getId());
        userExpandInfoManager.save(userExpandInfo);

        // 创建商户用户关联
        MerchantUser merchantUser = new MerchantUser(userInfo.getId(), mchNo, false);
        merchantUserManager.save(merchantUser);

        // 返回账号与初始密码明文, 供管理员一次性复制转告用户
        return new UserPasswordResult()
                .setUserId(userInfo.getId())
                .setAccount(userInfo.getAccount())
                .setName(userInfo.getName())
                .setPassword(password);
    }

    /// 编辑商户用户
    @Transactional(rollbackFor = Exception.class)
    public void update(MerchantUserParam param) {
        UserInfo userInfo = userInfoManager.findById(param.getId())
                .orElseThrow(UserInfoNotExistsException::new);

        MerchantUser merchantUser = merchantUserManager.findByUserId(param.getId())
                // 商户: 商户用户关联关系不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist"));

        // 按终端校验手机号唯一性（排除自身）
        if (StrUtil.isNotBlank(param.getPhone()) && 
            userQueryService.existsPhoneByClientCode(userInfo.getClientCode(), param.getPhone(), param.getId())) {
            // 商户: 该终端下手机号已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.phoneUsedByOtherInClient");
        }

        // 按终端校验邮箱唯一性（排除自身）
        if (StrUtil.isNotBlank(param.getEmail()) && 
            userQueryService.existsEmailByClientCode(userInfo.getClientCode(), param.getEmail(), param.getId())) {
            // 商户: 该终端下邮箱已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedByOtherInClient");
        }

        param.setPassword(null);
        param.setAccount(null);
        String oldEmail = userInfo.getEmail();
        MerchantUserConvert.CONVERT.copy(param, userInfo);
        // 管理员变更邮箱后原验证状态不再可信, 重置为未验证(需用户重新走邮箱验证流程)
        if (!Objects.equals(oldEmail, userInfo.getEmail())) {
            userInfo.setEmailVerified(false);
        }
        userInfoManager.updateById(userInfo);
    }

    /// 分配角色
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, Long roleId) {
        this.checkMerchantUser(userId);
        userRoleService.saveAssign(userId, roleId, false);
    }

    /// 封禁商户用户
    public void ban(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.ban(userId);
    }

    /// 批量封禁商户用户
    public void banBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.banBatch(userIds);
    }

    /// 解锁商户用户
    public void unlock(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.unlock(userId);
    }

    /// 批量解锁商户用户
    public void unlockBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.unlockBatch(userIds);
    }

    /// 重置密码
    /// 密码统一由系统按密码策略随机生成, 不接受调用方指定
    /// @return 含初始密码明文的结果
    @Transactional(rollbackFor = Exception.class)
    public UserPasswordResult restartPassword(Long userId) {
        this.checkMerchantUser(userId);
        return userAdminService.restartPassword(userId);
    }

    /// 批量重置密码
    /// @return 每个用户独立的初始密码结果
    @Transactional(rollbackFor = Exception.class)
    public List<UserPasswordResult> restartPasswordBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        return userAdminService.restartPasswordBatch(userIds);
    }

    /// 按平台密码策略计算过期时间(UTC); 未启用轮换则 null
    private OffsetDateTime calculatePasswordExpireTime() {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy();
        Integer rotationDays = config.getRotationDays();
        if (rotationDays == null || rotationDays <= 0) {
            return null;
        }
        return OffsetDateTime.now(ZoneOffset.UTC).plusDays(rotationDays);
    }

    /// 校验用户是否属于商户
    private void checkMerchantUser(Long userId) {
        if (!merchantUserManager.existedByField(MerchantUser::getUserId, userId)) {
            // 商户: 商户用户关联关系不存在
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist");
        }
    }

    /// 批量校验用户是否属于商户
    private void checkMerchantUser(List<Long> userIds) {
        List<MerchantUser> users = merchantUserManager.findAllByField(MerchantUser::getUserId, userIds);
        if (users.size() != userIds.size()) {
            // 商户: 商户用户关联关系不存在
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist");
        }
    }
}
