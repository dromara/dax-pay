package cn.daxpay.single.admin.controller.extra;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.daxpay.single.service.core.extra.AliPayAuthService;
import cn.daxpay.single.service.dto.extra.AuthUrlResult;
import cn.daxpay.single.service.dto.extra.OpenIdResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝认证控制器
 * @author xxm
 * @since 2024/6/16
 */
@IgnoreAuth
@Tag(name = "支付宝认证控制器")
@RestController
@RequestMapping("/alipay/auth")
@RequiredArgsConstructor
public class AliPayAuthController {
    private final AliPayAuthService aliPayAuthService;

    @Operation(summary = "返回获取OpenId授权页面地址和标识码")
    @PostMapping("/generateAuthUrl")
    public ResResult<AuthUrlResult> generateAuthUrl(){
        return Res.ok(aliPayAuthService.generateAuthUrl());
    }

    @Operation(summary = "根据标识码查询OpenId")
    @GetMapping("/queryOpenId")
    public ResResult<OpenIdResult> queryOpenId(String code){
        return Res.ok(aliPayAuthService.queryOpenId(code));
    }
}
