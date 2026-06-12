package org.dromara.daxpay.payment.merchant.controller.info;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.merchant.param.info.MerchantForgotParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantRegisterParam;
import org.dromara.daxpay.payment.merchant.service.user.MerchantUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/// # 商户管理
///
@IgnoreAuth
@Tag(name = "商户管理")
@RestController
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MerchantUserController {
    private final MerchantUserService merchantUserService;

    @Operation(summary = "商户注册")
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody MerchantRegisterParam param){
        merchantUserService.register(param);
        return Res.ok();
    }

    @Operation(summary = "发送注册验证码")
    @PostMapping("/register/send-captcha")
    public Result<Void> sendRegisterCaptcha(@NotBlank(message = "{validation.field.phone.notBlank}") String phone) {
        merchantUserService.sendRegisterCaptcha(phone);
        return Res.ok();
    }

    @Operation(summary = "校验注册验证码")
    @GetMapping("/register/check-captcha")
    public Result<Void> checkRegisterCaptcha(@NotBlank(message = "{validation.field.phone.notBlank}") String phone,
        @NotBlank(message = "{validation.field.captcha.notBlank}") String smsCaptcha) {
        merchantUserService.checkRegisterCaptcha(phone, smsCaptcha);
        return Res.ok();
    }

    @Operation(summary = "发送忘记密码验证码")
    @PostMapping("/forgot/send-captcha")
    public Result<String> sendForgotCaptcha(@NotBlank(message = "{validation.field.account.notBlank}") String account,
        @NotBlank(message = "{validation.field.phone.notBlank}") String phone) {
        return Res.ok(merchantUserService.sendForgotCaptcha(account, phone));
    }

    @Operation(summary = "校验忘记密码验证码")
    @GetMapping("/forgot/check-captcha")
    public Result<Void> checkForgotCaptcha(@Valid MerchantForgotParam param) {
        merchantUserService.checkForgotCaptcha(param);
        return Res.ok();
    }

    @Operation(summary = "修改密码")
    @PostMapping("/forgot/change-pwd")
    public Result<Void> forgot(@Validated @RequestBody MerchantForgotParam param) {
        merchantUserService.forgot(param);
        return Res.ok();
    }
}
