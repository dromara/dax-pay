package cn.daxpay.open.channel.ums.controller.direct;

import cn.daxpay.open.channel.ums.param.direct.UmsDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectChannelMerchantResult;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectKeyConfigResult;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectChannelMerchantService;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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

/// # 银联商务直连通道商户管理
///
/// 提供通道商户创建、查询和密钥配置管理。
/// 银联商务签名无证书, 密钥配置为 umsAppId/appKey/secretKey 三件套。
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "银联商务直连通道商户管理")
@RestController
@RequestMapping("/admin/ums/direct-channel-merchant")
@RequiredArgsConstructor
public class UmsDirectChannelMerchantController {

    private final UmsDirectChannelMerchantService umsDirectChannelMerchantService;
    private final UmsDirectKeyConfigService umsDirectKeyConfigService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询银联商务直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<UmsDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(umsDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建银联商务直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated UmsDirectChannelMerchantCreateParam param) {
        umsDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<UmsDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = umsDirectKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setAppKeyConfigured(config.getAppKey() != null);
        result.setSecretKeyConfigured(config.getSecretKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated UmsDirectKeyConfigParam param) {
        umsDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
