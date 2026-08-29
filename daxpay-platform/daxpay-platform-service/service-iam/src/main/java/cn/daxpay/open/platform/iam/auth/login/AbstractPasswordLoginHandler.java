package cn.daxpay.open.platform.iam.auth.login;

import cn.daxpay.open.platform.capability.auth.authentication.Authenticator;
import cn.daxpay.open.platform.capability.auth.code.AuthLoginTypeCode;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.exception.auth.UserNotFoundException;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/// # 账号密码登录基类
///
/// 承载密码登录的通用流程(用户定位、验证码校验、密码比对、登录重试状态注入)。
/// 子类只需声明身份域编码 [#getClientCode], 由认证框架按"身份域 + 登录方式"双键路由,
/// 从而消除多终端同登录方式(如平台端/商户端均为 password)的 findFirst 歧义与重复代码。
///
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPasswordLoginHandler implements Authenticator {

    protected static final String ACCOUNT_PARAMETER = "account";

    protected static final String PASSWORD_PARAMETER = "password";

    protected static final String CAPTCHA_KEY_PARAMETER = "captchaKey";

    protected static final String CAPTCHA_CODE_PARAMETER = "captchaCode";

    @Resource
    protected UserQueryService userQueryService;

    protected final LoginRetryService loginRetryService;

    protected final CaptchaService captchaService;

    protected final IamSecurityConfigService iamSecurityConfigService;

    protected final PasswordDecryptService passwordDecryptService;

    /// 登录方式固定为账号密码
    @Override
    public final String getLoginType() {
        return AuthLoginTypeCode.PASSWORD;
    }

    /// 认证
    @Override
    public @NotNull AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        HttpServletRequest request = context.getRequest();
        String account = this.obtainAccount(request);
        String password = this.obtainPassword(request);
        String captchaKey = this.obtainCaptchaKey(request);
        String captchaCode = this.obtainCaptchaCode(request);
        // 从请求上下文获取身份域编码，按身份域+账号查询用户
        String clientCode = context.getClientCode();

        UserInfoResult userInfoResult = this.loadUserByClientCodeAndAccount(clientCode, account);
        UserDetail userDetail = userInfoResult.toUserDetail();

        // 检查验证码（如果需要）
        this.checkCaptcha(userDetail.getId(), captchaKey, captchaCode);
        loginRetryService.checkBeforeLogin(userDetail);

        // 比对密码未通过
        if (!BCrypt.checkpw(password, userInfoResult.getPassword())) {
            // 必须携带 userId, 否则 LoginRetryService.onLoginFailure 因 userId 为空直接跳过, 失败计数始终为 0
            throw new LoginFailureException(userDetail.getId(), userDetail.getAccount(), "error.auth.accountOrPasswordError");
        }

        // 设置密码状态到 UserDetail（超级管理员不设置密码状态限制）
        if (!userDetail.isAdmin()) {
            loginRetryService.setPasswordStatusToUserDetail(userDetail);
        }

        return new AuthInfoResult().setId(userDetail.getId()).setUserDetail(userDetail);
    }

    /// 检查验证码
    protected void checkCaptcha(Long userId, String captchaKey, String captchaCode) {
        PlatformLoginSecurityConfig config = iamSecurityConfigService.getLoginSecurity();
        if (!Boolean.TRUE.equals(config.getCaptchaEnabled())) {
            return;
        }
        int triggerAttempts = ObjectUtil.defaultIfNull(config.getCaptchaTriggerAttempts(),
                PlatformLoginSecurityConfig.DEFAULT_CAPTCHA_TRIGGER_ATTEMPTS);
        int errorCount = loginRetryService.getErrorCount(userId);
        captchaService.checkOrValidateCaptcha(errorCount, triggerAttempts, captchaKey, captchaCode);
    }

    /// 根据身份域编码+账号加载用户（身份域维度用户定位）
    protected UserInfoResult loadUserByClientCodeAndAccount(String clientCode, String account) {
        // 按终端+账号查询用户，跨终端同名账号不会命中
        UserInfoResult userInfoResult = userQueryService.findByClientCodeAndAccount(clientCode, account);
        if (Objects.isNull(userInfoResult)) {
            throw new UserNotFoundException(account);
        }
        return userInfoResult;
    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        String password = request.getParameter(PASSWORD_PARAMETER);
        return passwordDecryptService.decryptPassword(password);
    }

    @Nullable
    protected String obtainAccount(HttpServletRequest request) {
        return request.getParameter(ACCOUNT_PARAMETER);
    }

    @Nullable
    protected String obtainCaptchaKey(HttpServletRequest request) {
        return request.getParameter(CAPTCHA_KEY_PARAMETER);
    }

    @Nullable
    protected String obtainCaptchaCode(HttpServletRequest request) {
        return request.getParameter(CAPTCHA_CODE_PARAMETER);
    }
}
