package cn.daxpay.open.channel.shouqianba.controller.direct;

import cn.daxpay.open.channel.shouqianba.param.direct.ShouqianbaDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.shouqianba.param.direct.ShouqianbaDirectKeyConfigParam;
import cn.daxpay.open.channel.shouqianba.result.direct.ShouqianbaDirectKeyConfigResult;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectChannelMerchantService;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectKeyConfigService;
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

/// # 收钱吧直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 终端号(terminal_sn)创建时录入, 终端密钥(terminal_key)由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "收钱吧直连通道商户管理")
@RestController
@RequestMapping("/admin/shouqianba/direct-channel-merchant")
@RequiredArgsConstructor
public class ShouqianbaDirectChannelMerchantController {

    private final ShouqianbaDirectChannelMerchantService shouqianbaDirectChannelMerchantService;
    private final ShouqianbaDirectKeyConfigService shouqianbaDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建收钱吧直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated ShouqianbaDirectChannelMerchantCreateParam param) {
        shouqianbaDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<ShouqianbaDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = shouqianbaDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setTerminalKeyConfigured(Objects.nonNull(config.getTerminalKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated ShouqianbaDirectKeyConfigParam param) {
        shouqianbaDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
