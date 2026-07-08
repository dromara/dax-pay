package cn.daxpay.open.channel.yeepay.controller.direct;

import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.yeepay.param.direct.YeepayDirectKeyConfigParam;
import cn.daxpay.open.channel.yeepay.result.direct.YeepayDirectKeyConfigResult;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectChannelMerchantService;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易宝直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(merchantNo/yopIsvNo)创建时录入, 密钥(appKey/privateKey/yopPublicKey等)由密钥配置维护。
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "易宝直连通道商户管理")
@RestController
@RequestMapping("/admin/yeepay/direct-channel-merchant")
@RequiredArgsConstructor
public class YeepayDirectChannelMerchantController {

    private final YeepayDirectChannelMerchantService yeepayDirectChannelMerchantService;
    private final YeepayDirectKeyConfigService yeepayDirectKeyConfigService;

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建易宝直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated YeepayDirectChannelMerchantCreateParam param) {
        yeepayDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<YeepayDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = yeepayDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setAppKeyConfigured(config.getAppKey() != null);
        result.setPrivateKeyConfigured(config.getPrivateKey() != null);
        result.setYopPublicKeyConfigured(config.getYopPublicKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated YeepayDirectKeyConfigParam param) {
        yeepayDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
