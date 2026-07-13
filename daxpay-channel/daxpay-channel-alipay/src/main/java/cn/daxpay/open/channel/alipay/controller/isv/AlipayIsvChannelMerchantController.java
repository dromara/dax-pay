package cn.daxpay.open.channel.alipay.controller.isv;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAppAuthTokenUpdateParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAuthParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAuthUrlResult;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvAuthService;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvChannelMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝服务商通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "支付宝服务商通道商户管理")
@RestController
@RequestMapping("/admin/alipay/isv-channel-merchant")
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantController {

    private final AlipayIsvChannelMerchantService alipayIsvChannelMerchantService;
    private final AlipayIsvAuthService alipayIsvAuthService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据通道商户号查询支付宝服务商通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "创建支付宝服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AlipayIsvChannelMerchantCreateParam param) {
        alipayIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "更新应用授权令牌")
    @PostMapping("/update-app-auth-token")
    public Result<Void> updateAppAuthToken(@RequestBody @Validated AlipayIsvAppAuthTokenUpdateParam param) {
        alipayIsvChannelMerchantService.updateAppAuthToken(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "生成代运营授权链接")
    @PostMapping("/gen-auth-url")
    public Result<AlipayIsvAuthUrlResult> genAuthUrl(@RequestBody @Validated AlipayIsvAuthParam param) {
        return Res.ok(alipayIsvAuthService.genAuthUrl(param));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "获取代运营授权回调地址")
    @GetMapping("/auth-callback-url")
    public Result<String> getAuthCallbackUrl() {
        return Res.ok(alipayIsvAuthService.getAuthCallbackUrl());
    }
}
