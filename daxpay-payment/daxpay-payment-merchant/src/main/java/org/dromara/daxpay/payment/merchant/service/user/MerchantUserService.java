package org.dromara.daxpay.payment.merchant.service.user;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.iam.auth.service.PasswordDecryptService;
import org.dromara.daxpay.platform.iam.dao.role.RoleManager;
import org.dromara.daxpay.platform.iam.dao.user.UserInfoManager;
import org.dromara.daxpay.platform.iam.entity.role.Role;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.service.upms.UserRoleService;
import org.dromara.daxpay.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.platform.core.exception.BizException;
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
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.dromara.daxpay.platform.core.code.CommonCode;

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

    // 短信验证码前缀
    private final static String SMS_CAPTCHA_KEY = "sms:captcha:merchant:forgot:";
    // 注册短信验证码前缀
    private final static String SMS_REGISTER_CAPTCHA_KEY = "sms:captcha:merchant:register:";

    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppInfoService mchAppInfoService;
    private final UserInfoManager userInfoManager;
    private final MerchantUserManager merchantUserManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectProvider<SmsReadConfig> smsReadConfigProvider;
    private final UserAdminService userAdminService;
    private final RoleManager roleManager;
    private final UserRoleService userRoleService;
    private final PasswordDecryptService passwordDecryptService;
    private final PaymentContext apiContext;

    /// 发送注册验证码
    public void sendRegisterCaptcha(String phone) {
        // 判断该手机号是否已注册
        boolean phoneExists = userInfoManager.findByClientCodeAndPhone("mch", phone).isPresent();
        if (phoneExists) {
            // 商户: 该手机号已注册
            throw new ValidationFailedException("error.payment.merchant.phoneRegistered");
        }

        // 检查手机号发送短信次数是否已经超出次数
        int smsCaptcha = RandomUtil.randomInt(100000, 999999);
        // 发送验证码, 有效期五分钟, 写入数据库
        redisTemplate.opsForValue()
                .set(SMS_REGISTER_CAPTCHA_KEY + phone, String.valueOf(smsCaptcha), 5, TimeUnit.MINUTES);
        log.info("手机号: {} 发送验证码: {}", phone, smsCaptcha);

        // 发送前进行初始化
        SmsFactory.createSmsBlend(getSmsReadConfig(),"def");
        SmsBlend smsBlend = SmsFactory.getSmsBlend("def");
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("code", String.valueOf(smsCaptcha));
        // 发送验证码
        SmsResponse smsResponse = smsBlend.sendMessage(phone, params);
        if (!smsResponse.isSuccess()){
            log.error("发送验证码失败: {}", smsResponse.getData());
            // 商户: 发送验证码失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.smsSendFailed");
        }
    }

    /// 注册验证码校验
    public void checkRegisterCaptcha(String phone, String smsCaptcha) {
        // 查询验证码, 判断是否一致
        String captcha = redisTemplate.opsForValue()
                .get(SMS_REGISTER_CAPTCHA_KEY + phone);
        if (!StrUtil.equals(smsCaptcha, captcha)) {
            // 商户: 验证码错误或已过期
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.smsCodeError");
        }
    }

    /// 注册商户
    @Transactional(rollbackFor = Exception.class)
    public void register(MerchantRegisterParam param) {
        // 校验注册验证码
        this.checkRegisterCaptcha(param.getPhone(), param.getSmsCaptcha());

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

        // 清空验证码
        redisTemplate.opsForValue().getAndDelete(SMS_REGISTER_CAPTCHA_KEY + param.getPhone());
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

    /// 获取短信配置
    private SmsReadConfig getSmsReadConfig() {
        SmsReadConfig smsReadConfig = smsReadConfigProvider.getIfAvailable();
        if (smsReadConfig == null) {
            // 商户: 短信配置不存在，请先完成短信配置
            throw new ConfigNotExistException("error.payment.merchant.smsConfigNotExist");
        }
        return smsReadConfig;
    }

    /// 发送找后密码验证码
    public String sendForgotCaptcha(String account, String phone) {
        // 按服务商+账号查找用户
        UserInfo userInfo = userInfoManager.findByClientCodeAndAccount("mch", account)
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        MerchantUser merchantUser = merchantUserManager.findByUserId(userInfo.getId())
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        if (!merchantInfoManager.existedByMchNo(merchantUser.getMchNo())) {
            // 商户: 账号或手机号未找到
            throw new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound");
        }
        // 判断密码是否一致
        if (!Objects.equals(phone, userInfo.getPhone())) {
            // 商户: 账号或手机号信息不正确
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.merchant.accountOrPhoneError");
        }

        // 检查手机号发送短信次数是否已经超出次数

        // 发送验证码, 有效期五分钟
        int smsCaptcha = RandomUtil.randomInt(100000, 999999);
        // 发送前进行初始化
        SmsFactory.createSmsBlend(getSmsReadConfig(),"def");
        SmsBlend smsBlend = SmsFactory.getSmsBlend("def");
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("code", String.valueOf(smsCaptcha));
        log.info("手机号: {} 发送验证码: {}", phone, smsCaptcha);
        // 发送验证码
        SmsResponse smsResponse = smsBlend.sendMessage(phone, params);
        if (!smsResponse.isSuccess()){
            log.error("发送验证码失败: {}", smsResponse.getData());
            // 商户: 发送验证码失败
            throw new OperationFailException("error.payment.merchant.smsSendFailed");
        }
        // 返回脱敏手机号
        return DesensitizedUtil.mobilePhone(phone);
    }

    /// 忘记密码验证码校验
    public void checkForgotCaptcha(MerchantForgotParam param) {
        // 按服务商+账号查找用户
        UserInfo userInfo = userInfoManager.findByClientCodeAndAccount("mch", param.getAccount())
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        MerchantUser merchantUser = merchantUserManager.findByUserId(userInfo.getId())
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        if (!merchantInfoManager.existedByMchNo(merchantUser.getMchNo())) {
            // 商户: 账号或手机号未找到
            throw new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound");
        }
        // 判断手机号是否一致
        if (!Objects.equals(param.getPhone(), userInfo.getPhone())) {
            // 商户: 账号或手机号信息不正确
            throw new OperationFailException("error.payment.merchant.accountOrPhoneError");
        }
        // 查询验证码, 判断是否一致
        String phone = userInfo.getPhone();
        String smsCaptcha = redisTemplate.opsForValue()
                .get(SMS_CAPTCHA_KEY + phone);
        if (!StrUtil.equals(param.getSmsCaptcha(), smsCaptcha)) {
            // 商户: 验证码错误或已过期
            throw new OperationFailException("error.payment.merchant.smsCodeError");
        }
    }

    /// 忘记密码 - 重新设置密码
    @Transactional(rollbackFor = Exception.class)
    public void forgot(MerchantForgotParam param) {
        this.checkForgotCaptcha(param);
        // 按服务商+账号查找用户
        UserInfo userInfo = userInfoManager.findByClientCodeAndAccount("mch", param.getAccount())
                // 商户: 账号或手机号未找到
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.accountOrPhoneNotFound"));
        // 解密密码
        String newPassword = passwordDecryptService.decryptPassword(param.getNewPassword());
        // 修改密码
        var passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.updateById(userInfo);
        // 清空验证码
        redisTemplate.opsForValue().getAndDelete(SMS_CAPTCHA_KEY + param.getPhone());
    }
}
