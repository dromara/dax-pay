package cn.daxpay.open.payment.merchant.auth;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.capability.auth.authentication.AbstractAuthentication;
import cn.daxpay.open.platform.capability.auth.code.AuthLoginTypeCode;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.auth.exception.UserNotFoundException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 商户端密码登录处理器
///
@Slf4j
@Component
@RequiredArgsConstructor
public class MchPasswordLoginHandler implements AbstractAuthentication {

    @Getter
    private final String ACCOUNT_PARAMETER = "account";

    @Getter
    private final String PASSWORD_PARAMETER = "password";

    @Getter
    private final String CAPTCHA_KEY_PARAMETER = "captchaKey";

    @Getter
    private final String CAPTCHA_CODE_PARAMETER = "captchaCode";

    @Getter
    private final String REMEMBER_PARAMETER = "remember";

    @Resource
    @Getter
    private UserQueryService userQueryService;

    private final LoginRetryService loginRetryService;

    private final CaptchaService captchaService;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final PasswordDecryptService passwordDecryptService;

    private final PaymentContext apiContext;


    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.PASSWORD;
    }

    @Override
    public void authenticationBefore(LoginAuthContext context) {
        String rememberParam = context.getRequest().getParameter(this.REMEMBER_PARAMETER);
        boolean remember = rememberParam == null || Boolean.parseBoolean(rememberParam);
        context.setRemember(remember);
    }

    @Override
    public @NotNull AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        String account = this.obtainAccount(context.getRequest());
        String password = this.obtainPassword(context.getRequest());
        String captchaKey = this.obtainCaptchaKey(context.getRequest());
        String captchaCode = this.obtainCaptchaCode(context.getRequest());
        String clientCode = context.getClientCode();

        UserInfoResult userInfoResult = this.loadUserByClientCodeAndAccount(clientCode, account);
        UserDetail userDetail = userInfoResult.toUserDetail();

        // 检查验证码（如果需要）
        this.checkCaptcha(userDetail.getId(), captchaKey, captchaCode);
        loginRetryService.checkBeforeLogin(userDetail);

        // 比对密码未通过
        if (!BCrypt.checkpw(password, userInfoResult.getPassword())) {
            // 必须携带 userId, 否则 LoginRetryService.onLoginFailure 因 userId 为空直接跳过, 失败计数始终为 0
            throw new LoginFailureException(userDetail.getId(), userDetail.getAccount(), "账号或密码不正确");
        }

        // 设置密码状态到 UserDetail（超级管理员不设置密码状态限制）
        if (!userDetail.isAdmin()) {
            loginRetryService.setPasswordStatusToUserDetail(userDetail);
        }

        return new AuthInfoResult().setId(userDetail.getId()).setUserDetail(userDetail);
    }

    /// 检查验证码
    private void checkCaptcha(Long userId, String captchaKey, String captchaCode) {
        PlatformLoginSecurityConfig config = iamSecurityConfigService.getLoginSecurity();
        if (!Boolean.TRUE.equals(config.getCaptchaEnabled())) {
            return;
        }
        int triggerAttempts = config.getCaptchaTriggerAttempts() == null ? 3 : config.getCaptchaTriggerAttempts();
        int errorCount = loginRetryService.getErrorCount(userId);
        captchaService.checkOrValidateCaptcha(errorCount, triggerAttempts, captchaKey, captchaCode);
    }

    /// 根据终端编码+账号加载用户
    public UserInfoResult loadUserByClientCodeAndAccount(String clientCode, String account) throws UserNotFoundException {
        // 回退到默认查找方式（终端维度）
        UserInfoResult userInfoResult = userQueryService.findByClientCodeAndAccount(clientCode, account);
        if (Objects.isNull(userInfoResult)) {
            throw new UserNotFoundException(account);
        }
        return userInfoResult;
    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        String password = request.getParameter(this.PASSWORD_PARAMETER);
        return passwordDecryptService.decryptPassword(password);
    }

    @Nullable
    protected String obtainAccount(HttpServletRequest request) {
        return request.getParameter(this.ACCOUNT_PARAMETER);
    }

    @Nullable
    protected String obtainCaptchaKey(HttpServletRequest request) {
        return request.getParameter(this.CAPTCHA_KEY_PARAMETER);
    }

    @Nullable
    protected String obtainCaptchaCode(HttpServletRequest request) {
        return request.getParameter(this.CAPTCHA_CODE_PARAMETER);
    }
}

