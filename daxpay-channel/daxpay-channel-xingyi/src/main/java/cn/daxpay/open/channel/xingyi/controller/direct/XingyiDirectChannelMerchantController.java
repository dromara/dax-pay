package cn.daxpay.open.channel.xingyi.controller.direct;

import cn.daxpay.open.channel.xingyi.param.direct.XingyiDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.xingyi.param.direct.XingyiDirectKeyConfigParam;
import cn.daxpay.open.channel.xingyi.result.direct.XingyiDirectKeyConfigResult;
import cn.daxpay.open.channel.xingyi.service.direct.XingyiDirectChannelMerchantService;
import cn.daxpay.open.channel.xingyi.service.direct.XingyiDirectKeyConfigService;
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

/// # 星驿付直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(机构号/星驿付商户号)创建时录入, 商户公钥由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "星驿付直连通道商户管理")
@RestController
@RequestMapping("/admin/xingyi/direct-channel-merchant")
@RequiredArgsConstructor
public class XingyiDirectChannelMerchantController {

    private final XingyiDirectChannelMerchantService xingyiDirectChannelMerchantService;

    private final XingyiDirectKeyConfigService xingyiDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建星驿付直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated XingyiDirectChannelMerchantCreateParam param) {
        xingyiDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<XingyiDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = xingyiDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setMerchantPublicKeyConfigured(Objects.nonNull(config.getMerchantPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated XingyiDirectKeyConfigParam param) {
        xingyiDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
