package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.payment.merchant.convert.info.MerchantInfoConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.merchant.param.info.MerchantForgotParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantRegisterParam;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.platform.core.enums.role.RoleCodeEnum;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 商户用户管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserService {

    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppInfoService mchAppInfoService;
    private final UserInfoManager userInfoManager;
    private final MerchantUserManager merchantUserManager;
    private final UserAdminService userAdminService;
    private final RoleManager roleManager;
    private final UserRoleService userRoleService;
    private final PasswordDecryptService passwordDecryptService;
    private final PasswordPolicyService passwordPolicyService;
    private final UserPasswordSecurityManager passwordSecurityManager;
    private final IamSecurityConfigService iamSecurityConfigService;

    /// 根据用户id查询商户号
    public String findMchNoByUserId(Long userId) {
        return merchantUserManager.findByUserId(userId)
                .map(MerchantUser::getMchNo)
                .orElse(null);
    }

    /// 注册商户
    @Transactional(rollbackFor = Exception.class)
    public void register(MerchantRegisterParam param) {
        var merchant = MerchantInfoConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());
        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        this.createMerchantAdmin(param, merchant);
        merchantInfoManager.save(merchant);
        // 创建默认应用
        MchAppInfo mchApp = new MchAppInfo()
                .setAppName(merchant.getMchName() + "的默认应用")
                .setDefaultApp(true)
                .setStatus(MerchantStatusEnum.ENABLE.getCode());
        mchApp.setAppId(mchAppInfoService.generateAppId())
                .setMchNo(merchant.getMchNo());
        mchAppInfoManager.save(mchApp);
    }

    /// 创建商户管理员
    private void createMerchantAdmin(MerchantRegisterParam param, MerchantInfo merchant) {
        // 创建用户（跳过通用重复校验，因为已做服务商维度校验）
        var userInfoParam = new UserInfoParam();
        MerchantInfoConvert.CONVERT.copy(param, userInfoParam);
        userInfoParam.setName(merchant.getMchName() + "管理员");
        userInfoParam.setPhone(param.getPhone());
        userInfoParam.setClientCode(ClientEnum.MERCHANT.getCode());
        UserInfo userInfo = userAdminService.add(userInfoParam, true);
        Role role = roleManager.findByCode(RoleCodeEnum.MERCHANT_ADMIN.getCode())
                // 商户: 商户管理员角色不存在, 请检查
                .orElseThrow(() -> new ConfigNotExistException("error.payment.merchant.adminRoleNotExist"));
        userRoleService.saveAssign(userInfo.getId(), role.getId(), true);
        merchantUserManager.save(new MerchantUser(userInfo.getId(), merchant.getMchNo(), true));
        merchant.setAdminUserId(userInfo.getId());
    }

    /// 生成商户号
    private String getMchNo() {
        String mchNo = "M" + System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            if (!merchantInfoManager.existedByField(MerchantInfo::getMchNo, mchNo)) {
                return mchNo;
            }
            mchNo = "M" + System.currentTimeMillis();
        }
        // 商户: 商户号生成失败
        throw new OperationFailException("error.payment.merchant.mchNoGenFailed");
    }

    /// 忘记密码 - 重新设置密码
    ///
    /// 公开接口([IgnoreAuth]): 必须用绑定手机号核验身份, 禁止仅凭账号改密。
    @Transactional(rollbackFor = Exception.class)
    public void forgot(MerchantForgotParam param) {
        if (StrUtil.isBlank(param.getPhone())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.accountOrPhoneError");
        }
        // 按商户终端 + 账号查找用户
        UserInfo userInfo = userInfoManager.findByClientCodeAndAccount(
                        ClientEnum.MERCHANT.getCode(), param.getAccount())
                // 商户: 账号或手机号未找到(统一文案, 避免枚举账号是否存在)
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        // 绑定手机号核验: 未绑定或与请求不一致均拒绝
        if (StrUtil.isBlank(userInfo.getPhone())
                || !Objects.equals(userInfo.getPhone(), param.getPhone())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.merchant.accountOrPhoneError");
        }
        // 解密并校验密码策略
        String newPassword = passwordDecryptService.decryptPassword(param.getNewPassword());
        passwordPolicyService.validatePasswordHistory(userInfo.getId(), newPassword);
        passwordPolicyService.validatePassword(newPassword);
        String passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.updateById(userInfo);
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);
        passwordSecurityManager.updatePasswordExpireTime(userInfo.getId(), calculatePasswordExpireTime());
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
}
