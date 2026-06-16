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

    @Operation(summary = "修改密码")
    @PostMapping("/forgot/change-pwd")
    public Result<Void> forgot(@Validated @RequestBody MerchantForgotParam param) {
        merchantUserService.forgot(param);
        return Res.ok();
    }
}
