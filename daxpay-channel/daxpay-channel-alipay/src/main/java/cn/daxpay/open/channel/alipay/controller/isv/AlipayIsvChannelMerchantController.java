package cn.daxpay.open.channel.alipay.controller.isv;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAppAuthTokenUpdateParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
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
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "支付宝服务商通道商户管理")
@RestController
@RequestMapping("/admin/alipay/isv-channel-merchant")
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantController {

    private final AlipayIsvChannelMerchantService alipayIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询支付宝服务商通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建支付宝服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AlipayIsvChannelMerchantCreateParam param) {
        alipayIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "更新应用授权令牌")
    @PostMapping("/update-app-auth-token")
    public Result<Void> updateAppAuthToken(@RequestBody @Validated AlipayIsvAppAuthTokenUpdateParam param) {
        alipayIsvChannelMerchantService.updateAppAuthToken(param);
        return Res.ok();
    }
}
