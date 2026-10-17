package cn.daxpay.open.channel.longpay.controller.direct;

import cn.daxpay.open.channel.longpay.param.direct.LongpayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.longpay.param.direct.LongpayDirectKeyConfigParam;
import cn.daxpay.open.channel.longpay.result.direct.LongpayDirectKeyConfigResult;
import cn.daxpay.open.channel.longpay.service.direct.LongpayDirectChannelMerchantService;
import cn.daxpay.open.channel.longpay.service.direct.LongpayDirectKeyConfigService;
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

/// # 建行龙支付直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 建行商户三要素创建时录入, 两把公钥由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "建行龙支付直连通道商户管理")
@RestController
@RequestMapping("/admin/long/direct-channel-merchant")
@RequiredArgsConstructor
public class LongpayDirectChannelMerchantController {

    private final LongpayDirectChannelMerchantService longpayDirectChannelMerchantService;
    private final LongpayDirectKeyConfigService longpayDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建建行龙支付直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated LongpayDirectChannelMerchantCreateParam param) {
        longpayDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<LongpayDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = longpayDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantPublicKeyConfigured(Objects.nonNull(config.getMerchantPublicKey()));
        result.setCcbPublicKeyConfigured(Objects.nonNull(config.getCcbPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated LongpayDirectKeyConfigParam param) {
        longpayDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
