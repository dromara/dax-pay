package cn.daxpay.open.channel.stripe.controller;

import cn.daxpay.open.channel.stripe.param.StripeChannelMerchantCreateParam;
import cn.daxpay.open.channel.stripe.param.StripeKeyConfigParam;
import cn.daxpay.open.channel.stripe.result.StripeChannelMerchantResult;
import cn.daxpay.open.channel.stripe.result.StripeKeyConfigResult;
import cn.daxpay.open.channel.stripe.service.StripeChannelMerchantService;
import cn.daxpay.open.channel.stripe.service.StripeKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
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

/// # Stripe 通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(mchNo/accountId)创建时录入, 密钥(secretKey/publishableKey/webhookSecret)由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Stripe 通道商户管理")
@RestController
@RequestMapping("/admin/stripe/channel-merchant")
@RequiredArgsConstructor
public class StripeChannelMerchantController {

    private final StripeChannelMerchantService stripeDirectChannelMerchantService;
    private final StripeKeyConfigService stripeDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询 Stripe 通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<StripeChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(stripeDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建 Stripe 通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated StripeChannelMerchantCreateParam param) {
        stripeDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<StripeKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = stripeDirectKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setSecretKeyConfigured(config.getSecretKey() != null);
        result.setPublishableKeyConfigured(config.getPublishableKey() != null);
        result.setWebhookSecretConfigured(config.getWebhookSecret() != null);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated StripeKeyConfigParam param) {
        stripeDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
