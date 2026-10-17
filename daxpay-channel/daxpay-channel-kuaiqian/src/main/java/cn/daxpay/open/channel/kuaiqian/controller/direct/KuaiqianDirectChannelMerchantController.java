package cn.daxpay.open.channel.kuaiqian.controller.direct;

import cn.daxpay.open.channel.kuaiqian.param.direct.KuaiqianDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.kuaiqian.param.direct.KuaiqianDirectKeyConfigParam;
import cn.daxpay.open.channel.kuaiqian.result.direct.KuaiqianDirectKeyConfigResult;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectChannelMerchantService;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectKeyConfigService;
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

/// # 快钱直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(会员号/服务商子账户号/当面付商户终端号)创建时录入, 证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "快钱直连通道商户管理")
@RestController
@RequestMapping("/admin/kuaiqian/direct-channel-merchant")
@RequiredArgsConstructor
public class KuaiqianDirectChannelMerchantController {

    private final KuaiqianDirectChannelMerchantService kuaiqianDirectChannelMerchantService;
    private final KuaiqianDirectKeyConfigService kuaiqianDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建快钱直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated KuaiqianDirectChannelMerchantCreateParam param) {
        kuaiqianDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<KuaiqianDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = kuaiqianDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantCertConfigured(Objects.nonNull(config.getMerchantCertPfx()));
        result.setMerchantCertPasswordConfigured(Objects.nonNull(config.getMerchantCertPassword()));
        result.setPlatformCertConfigured(Objects.nonNull(config.getPlatformCert()));
        result.setSslCertConfigured(Objects.nonNull(config.getSslCertP12()));
        result.setSslCertPasswordConfigured(Objects.nonNull(config.getSslCertPassword()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated KuaiqianDirectKeyConfigParam param) {
        kuaiqianDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
