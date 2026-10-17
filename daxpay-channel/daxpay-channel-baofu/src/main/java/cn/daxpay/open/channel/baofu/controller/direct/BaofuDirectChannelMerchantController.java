package cn.daxpay.open.channel.baofu.controller.direct;

import cn.daxpay.open.channel.baofu.param.direct.BaofuDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.baofu.param.direct.BaofuDirectKeyConfigParam;
import cn.daxpay.open.channel.baofu.result.direct.BaofuDirectKeyConfigResult;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectChannelMerchantService;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectKeyConfigService;
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

import java.util.Objects;

/// # 宝付直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(宝付商户号/终端号)创建时录入, 证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "宝付直连通道商户管理")
@RestController
@RequestMapping("/admin/baofu/direct-channel-merchant")
@RequiredArgsConstructor
public class BaofuDirectChannelMerchantController {

    private final BaofuDirectChannelMerchantService baofuDirectChannelMerchantService;
    private final BaofuDirectKeyConfigService baofuDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建宝付直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated BaofuDirectChannelMerchantCreateParam param) {
        baofuDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<BaofuDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = baofuDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantCertConfigured(Objects.nonNull(config.getMerchantCertPfx()));
        result.setMerchantCertPasswordConfigured(Objects.nonNull(config.getMerchantCertPassword()));
        result.setPlatformCertConfigured(Objects.nonNull(config.getPlatformCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated BaofuDirectKeyConfigParam param) {
        baofuDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
