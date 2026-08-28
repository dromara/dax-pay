package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.NonceVerification;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.email.PasswordForgetService;
import cn.daxpay.open.platform.iam.param.auth.ForgetResetPasswordParam;
import cn.daxpay.open.platform.iam.param.auth.ForgetSendCodeParam;
import cn.daxpay.open.platform.iam.result.auth.ForgetSendCodeResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 找回密码端点(登录页公开接口)
///
@Validated
@IgnoreAuth
@Tag(name = "找回密码")
@RestController
@RequestMapping("/token/forget")
@AllArgsConstructor
public class PasswordForgetEndpoint {

    private final PasswordForgetService passwordForgetService;

    @Operation(summary = "发送找回密码验证码")
    @NonceVerification
    @PostMapping("/send-code")
    public Result<ForgetSendCodeResult> sendCode(@RequestBody @Validated ForgetSendCodeParam param) {
        return Res.ok(passwordForgetService.sendCode(param));
    }

    @Operation(summary = "重置密码")
    @NonceVerification
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody @Validated ForgetResetPasswordParam param) {
        passwordForgetService.resetPassword(param);
        return Res.ok();
    }
}
