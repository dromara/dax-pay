package org.dromara.daxpay.platform.iam.endpoint;

import org.dromara.daxpay.platform.common.config.properties.PlatformConfigProperties;
import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.annotation.NonceVerification;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.iam.auth.service.LoginContentService;
import org.dromara.daxpay.platform.iam.auth.service.LoginSmsCaptchaService;
import org.dromara.daxpay.platform.iam.auth.service.SecondCheckService;
import org.dromara.daxpay.platform.iam.param.auth.LoginContentParam;
import org.dromara.daxpay.platform.iam.param.auth.LoginSmsCaptchaSendParam;
import org.dromara.daxpay.platform.iam.result.auth.LoginContentResult;
import org.dromara.daxpay.platform.iam.result.auth.SecondCheckResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 基础登录退出操作
///
@Validated
@IgnoreAuth
@Tag(name = "认证相关")
@RestController
@RequestMapping("/token")
@AllArgsConstructor
public class TokenEndpoint {
    private final TokenService tokenService;

    private final LoginContentService loginContentService;

    private final LoginSmsCaptchaService loginSmsCaptchaService;

    private final SecondCheckService secondCheckService;

    private final PlatformConfigProperties platformConfigProperties;

    @Operation(summary = "获取RSA公钥")
    @GetMapping("/public-key")
    public Result<String> getPublicKey() {
        return Res.ok(platformConfigProperties.getKeyConfig().getPublicKey());
    }

    @Operation(summary = "普通登录")
    @NonceVerification
    @PostMapping("/login")
    public Result<String> login(HttpServletRequest request, HttpServletResponse response) {
        return Res.ok(tokenService.login(request, response));
    }

    @Operation(summary = "获取登录页上下文")
    @PostMapping("/login-content")
    public Result<LoginContentResult> getLoginContent(@RequestBody(required = false) LoginContentParam param) {
        return Res.ok(loginContentService.getLoginContent(param == null ? new LoginContentParam() : param));
    }

    @Operation(summary = "发送登录短信验证码")
    @PostMapping("/send-sms-captcha")
    public Result<Void> sendSmsCaptcha(@RequestBody @Valid LoginSmsCaptchaSendParam param) {
        loginSmsCaptchaService.sendSmsCaptcha(param);
        return Res.ok();
    }

    @Operation(summary = "获取二次校验信息")
    @PostMapping("/second-check")
    public Result<SecondCheckResult> getSecondCheck(@RequestBody(required = false) LoginContentParam param) {
        return Res.ok(secondCheckService.getSecondCheck(param == null ? new LoginContentParam() : param));
    }

    @Operation(summary = "退出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        tokenService.logout();
        return Res.ok();
    }

}
