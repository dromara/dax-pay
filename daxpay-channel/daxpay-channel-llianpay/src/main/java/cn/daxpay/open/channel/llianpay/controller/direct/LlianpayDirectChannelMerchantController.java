package cn.daxpay.open.channel.llianpay.controller.direct;

import cn.daxpay.open.channel.llianpay.param.direct.LlianpayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.llianpay.param.direct.LlianpayDirectKeyConfigParam;
import cn.daxpay.open.channel.llianpay.result.direct.LlianpayDirectKeyConfigResult;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectChannelMerchantService;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectKeyConfigService;
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

/// # 连连支付直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(连连商户编号/业务参数)创建时录入, 商户私钥与连连平台公钥由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "连连支付直连通道商户管理")
@RestController
@RequestMapping("/admin/llianpay/direct-channel-merchant")
@RequiredArgsConstructor
public class LlianpayDirectChannelMerchantController {

    private final LlianpayDirectChannelMerchantService llianpayDirectChannelMerchantService;
    private final LlianpayDirectKeyConfigService llianpayDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建连连直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated LlianpayDirectChannelMerchantCreateParam param) {
        llianpayDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<LlianpayDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = llianpayDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setLlianpayPublicKeyConfigured(Objects.nonNull(config.getLlianpayPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated LlianpayDirectKeyConfigParam param) {
        llianpayDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
