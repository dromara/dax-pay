package cn.daxpay.open.channel.fubei.controller.direct;

import cn.daxpay.open.channel.fubei.param.direct.FubeiDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.fubei.param.direct.FubeiDirectKeyConfigParam;
import cn.daxpay.open.channel.fubei.result.direct.FubeiDirectKeyConfigResult;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectChannelMerchantService;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectKeyConfigService;
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

/// # 付呗直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(开放平台应用ID/商户ID/门店ID)创建时录入, 接口密钥由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "付呗直连通道商户管理")
@RestController
@RequestMapping("/admin/fubei/direct-channel-merchant")
@RequiredArgsConstructor
public class FubeiDirectChannelMerchantController {

    private final FubeiDirectChannelMerchantService fubeiDirectChannelMerchantService;
    private final FubeiDirectKeyConfigService fubeiDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建付呗直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated FubeiDirectChannelMerchantCreateParam param) {
        fubeiDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<FubeiDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = fubeiDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setAppSecretConfigured(Objects.nonNull(config.getAppSecret()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated FubeiDirectKeyConfigParam param) {
        fubeiDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
