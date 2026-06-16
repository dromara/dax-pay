package org.dromara.daxpay.payment.merchant.service.user;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.merchant.convert.info.MerchantInfoConvert;
import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantInfoManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantUserManager;
import org.dromara.daxpay.payment.merchant.entity.appinfo.MchAppInfo;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantUser;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.param.info.MerchantForgotParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantRegisterParam;
import org.dromara.daxpay.payment.merchant.service.appinfo.MchAppInfoService;
import org.dromara.daxpay.platform.core.enums.role.RoleCodeEnum;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.dromara.daxpay.platform.iam.auth.service.PasswordDecryptService;
import org.dromara.daxpay.platform.iam.dao.role.RoleManager;
import org.dromara.daxpay.platform.iam.dao.user.UserInfoManager;
import org.dromara.daxpay.platform.iam.entity.role.Role;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.service.upms.UserRoleService;
import org.dromara.daxpay.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.payment.common.context.PaymentContext;

/// # 商户用户管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserService {

    /// 根据用户id查询商户号
    public String findByUserId(Long userId){
        return merchantUserManager.findByUserId(userId)
                .map(MerchantUser::getMchNo)
                .orElse(null);
    }

    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppInfoService mchAppInfoService;
    private final UserInfoManager userInfoManager;
    private final MerchantUserManager merchantUserManager;
    private final UserAdminService userAdminService;
    private final RoleManager roleManager;
    private final UserRoleService userRoleService;
    private final PasswordDecryptService passwordDecryptService;
    private final PaymentContext apiContext;

    /// 注册商户
    @Transactional(rollbackFor = Exception.class)
    public void register(MerchantRegisterParam param) {
        var merchant = MerchantInfoConvert.CONVERT.toEntity(param);
        merchant.setMchNo(this.getMchNo());
        merchant.setStatus(MerchantStatusEnum.ENABLE.getCode());
        // 创建商户管理员
        this.createMerchantAdmin(param, merchant);
        merchantInfoManager.save(merchant);
        // 是否创建创建默认应用
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
    private String getMchNo(){
        String mchNo = "M" + System.currentTimeMillis();
        for (int i = 0; i < 10; i++){
            if (!merchantInfoManager.existedByField(MerchantInfo::getMchNo, mchNo)){
                return mchNo;
            }
            mchNo = "M" + System.currentTimeMillis();
        }
        // 商户: 商户号生成失败
        throw new OperationFailException("error.payment.merchant.mchNoGenFailed");
    }

    /// 忘记密码 - 重新设置密码
    @Transactional(rollbackFor = Exception.class)
    public void forgot(MerchantForgotParam param) {
        // 按服务商+账号查找用户
        UserInfo userInfo = userInfoManager.findByClientCodeAndAccount("mch", param.getAccount())
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new org.dromara.daxpay.platform.core.exception.DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        // 解密密码
        String newPassword = passwordDecryptService.decryptPassword(param.getNewPassword());
        // 修改密码
        var passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.updateById(userInfo);
    }
}
