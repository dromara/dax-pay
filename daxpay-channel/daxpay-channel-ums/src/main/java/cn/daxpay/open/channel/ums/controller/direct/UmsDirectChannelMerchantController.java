package cn.daxpay.open.channel.ums.controller.direct;

import cn.daxpay.open.channel.ums.param.direct.UmsDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectKeyConfigResult;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectChannelMerchantService;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
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

/// # 银联商务直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(mid)创建时录入, 应用ID/终端号(tid)/应用密钥/通讯密钥由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "银联商务直连通道商户管理")
@RestController
@RequestMapping("/admin/ums/direct-channel-merchant")
@RequiredArgsConstructor
public class UmsDirectChannelMerchantController {

    private final UmsDirectChannelMerchantService umsDirectChannelMerchantService;
    private final UmsDirectKeyConfigService umsDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建银联商务直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated UmsDirectChannelMerchantCreateParam param) {
        umsDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<UmsDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = umsDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setAppKeyConfigured(config.getAppKey() != null);
        result.setSecretKeyConfigured(config.getSecretKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated UmsDirectKeyConfigParam param) {
        umsDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
