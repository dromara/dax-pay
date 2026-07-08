package cn.daxpay.open.channel.adapay.controller.direct;

import cn.daxpay.open.channel.adapay.param.direct.AdapayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.adapay.param.direct.AdapayDirectKeyConfigParam;
import cn.daxpay.open.channel.adapay.result.direct.AdapayDirectKeyConfigResult;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectChannelMerchantService;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectKeyConfigService;
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

/// # Adapay 直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// Adapay 应用 ID/API Key/私钥/公钥 由密钥配置维护。
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "Adapay 直连通道商户管理")
@RestController
@RequestMapping("/admin/adapay/direct-channel-merchant")
@RequiredArgsConstructor
public class AdapayDirectChannelMerchantController {

    private final AdapayDirectChannelMerchantService adapayDirectChannelMerchantService;
    private final AdapayDirectKeyConfigService adapayDirectKeyConfigService;

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建Adapay 直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AdapayDirectChannelMerchantCreateParam param) {
        adapayDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<AdapayDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = adapayDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setApiKeyConfigured(config.getApiKey() != null);
        result.setPrivateKeyConfigured(config.getPrivateKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated AdapayDirectKeyConfigParam param) {
        adapayDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
