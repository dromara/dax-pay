package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.NonceVerification;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.LoginContentService;
import cn.daxpay.open.platform.iam.auth.service.SecondCheckService;
import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.param.auth.SecondVerifyParam;
import cn.daxpay.open.platform.iam.result.auth.LoginContentResult;
import cn.daxpay.open.platform.iam.result.auth.SecondCheckResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Operation(summary = "获取二次校验信息")
    @PostMapping("/second-check")
    public Result<SecondCheckResult> getSecondCheck(@RequestBody(required = false) LoginContentParam param) {
        return Res.ok(secondCheckService.getSecondCheck(param == null ? new LoginContentParam() : param));
    }

    @Operation(summary = "双因素认证二次验证")
    @PostMapping("/second-verify")
    public Result<String> secondVerify(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody SecondVerifyParam param) {
        return Res.ok(tokenService.secondVerify(request, response,
                param.getPreAuthToken(), param.getCode(), param.getCodeType()));
    }

    @Operation(summary = "退出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        tokenService.logout();
        return Res.ok();
    }

}
