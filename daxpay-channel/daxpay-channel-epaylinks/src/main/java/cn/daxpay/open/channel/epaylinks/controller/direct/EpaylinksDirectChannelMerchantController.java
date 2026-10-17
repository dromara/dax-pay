package cn.daxpay.open.channel.epaylinks.controller.direct;

import cn.daxpay.open.channel.epaylinks.param.direct.EpaylinksDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.epaylinks.param.direct.EpaylinksDirectKeyConfigParam;
import cn.daxpay.open.channel.epaylinks.result.direct.EpaylinksDirectKeyConfigResult;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectChannelMerchantService;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectKeyConfigService;
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

/// # 易票联直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(易票联商户编号/上游商户号)创建时录入, 密钥三元组由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "易票联直连通道商户管理")
@RestController
@RequestMapping("/admin/epaylinks/direct-channel-merchant")
@RequiredArgsConstructor
public class EpaylinksDirectChannelMerchantController {

    private final EpaylinksDirectChannelMerchantService epaylinksDirectChannelMerchantService;
    private final EpaylinksDirectKeyConfigService epaylinksDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建易票联直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated EpaylinksDirectChannelMerchantCreateParam param) {
        epaylinksDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<EpaylinksDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = epaylinksDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setSignNoConfigured(Objects.nonNull(config.getSignNo()));
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setPlatformCertConfigured(Objects.nonNull(config.getPlatformCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated EpaylinksDirectKeyConfigParam param) {
        epaylinksDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
