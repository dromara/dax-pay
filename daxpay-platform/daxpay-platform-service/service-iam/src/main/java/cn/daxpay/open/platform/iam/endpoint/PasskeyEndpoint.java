package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.NonceVerification;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.passkey.PasskeyService;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyLoginOptionsParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyLoginVerifyParam;
import cn.daxpay.open.platform.iam.result.passkey.PasskeyLoginOptionsResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通行密钥登录端点
///
/// 通行密钥登录为两阶段交互(先取认证选项再提交断言), 与单请求的 Authenticator SPI 模型不符,
/// 故与第三方社交登录同模式独立编排: 断言验证通过后走统一登录收尾。
/// 匿名可访问, login-verify 与密码登录同标准加防重放校验。
///
@IgnoreAuth
@Validated
@Tag(name = "通行密钥登录")
@RestController
@RequestMapping("/passkey")
@RequiredArgsConstructor
public class PasskeyEndpoint {

    private final PasskeyService passkeyService;

    /// 生成登录认证选项(discoverable 免输账号)
    @Operation(summary = "获取通行密钥登录选项")
    @PostMapping("/login-options")
    public Result<PasskeyLoginOptionsResult> loginOptions(@RequestBody @Validated PasskeyLoginOptionsParam param) {
        return Res.ok(passkeyService.loginOptions(param.getClient()));
    }

    /// 验证登录断言, 通过后返回登录 token
    @NonceVerification
    @Operation(summary = "通行密钥登录验证")
    @PostMapping("/login-verify")
    public Result<String> loginVerify(@RequestBody @Validated PasskeyLoginVerifyParam param,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        return Res.ok(passkeyService.verifyLogin(param, request, response));
    }
}
