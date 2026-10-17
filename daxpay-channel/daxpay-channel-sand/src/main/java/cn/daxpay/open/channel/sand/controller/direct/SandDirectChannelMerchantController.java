package cn.daxpay.open.channel.sand.controller.direct;

import cn.daxpay.open.channel.sand.param.direct.SandDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.sand.param.direct.SandDirectKeyConfigParam;
import cn.daxpay.open.channel.sand.result.direct.SandDirectKeyConfigResult;
import cn.daxpay.open.channel.sand.service.direct.SandDirectChannelMerchantService;
import cn.daxpay.open.channel.sand.service.direct.SandDirectKeyConfigService;
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

/// # 杉德直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(杉德商户编号 mid + 市场产品编码)创建时录入, 证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "杉德直连通道商户管理")
@RestController
@RequestMapping("/admin/sand/direct-channel-merchant")
@RequiredArgsConstructor
public class SandDirectChannelMerchantController {

    private final SandDirectChannelMerchantService sandDirectChannelMerchantService;
    private final SandDirectKeyConfigService sandDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建杉德直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated SandDirectChannelMerchantCreateParam param) {
        sandDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<SandDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = sandDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantCertConfigured(Objects.nonNull(config.getMerchantCertPfx()));
        result.setMerchantCertPasswordConfigured(Objects.nonNull(config.getMerchantCertPassword()));
        result.setSandCertConfigured(Objects.nonNull(config.getSandCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated SandDirectKeyConfigParam param) {
        sandDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
