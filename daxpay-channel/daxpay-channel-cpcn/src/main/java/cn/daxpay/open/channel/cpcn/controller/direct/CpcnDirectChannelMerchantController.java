package cn.daxpay.open.channel.cpcn.controller.direct;

import cn.daxpay.open.channel.cpcn.param.direct.CpcnDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.cpcn.param.direct.CpcnDirectKeyConfigParam;
import cn.daxpay.open.channel.cpcn.result.direct.CpcnDirectKeyConfigResult;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectChannelMerchantService;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectKeyConfigService;
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

/// # 中金直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(中金机构编号)创建时录入, 网关地址与证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "中金直连通道商户管理")
@RestController
@RequestMapping("/admin/cpcn/direct-channel-merchant")
@RequiredArgsConstructor
public class CpcnDirectChannelMerchantController {

    private final CpcnDirectChannelMerchantService cpcnDirectChannelMerchantService;
    private final CpcnDirectKeyConfigService cpcnDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建中金直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated CpcnDirectChannelMerchantCreateParam param) {
        cpcnDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<CpcnDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = cpcnDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantCertConfigured(Objects.nonNull(config.getMerchantCertPfx()));
        result.setMerchantCertPasswordConfigured(Objects.nonNull(config.getMerchantCertPassword()));
        result.setPlatformCertConfigured(Objects.nonNull(config.getPlatformCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated CpcnDirectKeyConfigParam param) {
        cpcnDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
