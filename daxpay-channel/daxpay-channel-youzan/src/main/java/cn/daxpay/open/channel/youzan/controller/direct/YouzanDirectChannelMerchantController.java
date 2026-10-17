package cn.daxpay.open.channel.youzan.controller.direct;

import cn.daxpay.open.channel.youzan.param.direct.YouzanDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.youzan.param.direct.YouzanDirectKeyConfigParam;
import cn.daxpay.open.channel.youzan.result.direct.YouzanDirectKeyConfigResult;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectChannelMerchantService;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectKeyConfigService;
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

/// # 有赞直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(高汇通商户号)创建时录入, RSA 密钥三元组(商户私钥/平台公钥/公钥标识)由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "有赞直连通道商户管理")
@RestController
@RequestMapping("/admin/youzan/direct-channel-merchant")
@RequiredArgsConstructor
public class YouzanDirectChannelMerchantController {

    private final YouzanDirectChannelMerchantService youzanDirectChannelMerchantService;
    private final YouzanDirectKeyConfigService youzanDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建有赞直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated YouzanDirectChannelMerchantCreateParam param) {
        youzanDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<YouzanDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = youzanDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setPlatformPublicKeyConfigured(Objects.nonNull(config.getPlatformPublicKey()));
        result.setPublicKeyIdConfigured(Objects.nonNull(config.getPublicKeyId()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated YouzanDirectKeyConfigParam param) {
        youzanDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
