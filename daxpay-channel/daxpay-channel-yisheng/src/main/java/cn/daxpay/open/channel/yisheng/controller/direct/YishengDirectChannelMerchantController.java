package cn.daxpay.open.channel.yisheng.controller.direct;

import cn.daxpay.open.channel.yisheng.param.direct.YishengDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.yisheng.param.direct.YishengDirectKeyConfigParam;
import cn.daxpay.open.channel.yisheng.result.direct.YishengDirectKeyConfigResult;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectChannelMerchantService;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectKeyConfigService;
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

/// # 易生直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(合作伙伴ID/商户编号)创建时录入, RSA 密钥对由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "易生直连通道商户管理")
@RestController
@RequestMapping("/admin/yisheng/direct-channel-merchant")
@RequiredArgsConstructor
public class YishengDirectChannelMerchantController {

    private final YishengDirectChannelMerchantService yishengDirectChannelMerchantService;
    private final YishengDirectKeyConfigService yishengDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建易生直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated YishengDirectChannelMerchantCreateParam param) {
        yishengDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<YishengDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = yishengDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setPlatformPublicKeyConfigured(Objects.nonNull(config.getPlatformPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated YishengDirectKeyConfigParam param) {
        yishengDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
