package cn.daxpay.open.channel.jeepay.controller.admin;

import cn.daxpay.open.channel.jeepay.param.JeepayKeyConfigParam;
import cn.daxpay.open.channel.jeepay.result.JeepayKeyConfigResult;
import cn.daxpay.open.channel.jeepay.service.config.JeepayKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # Jeepay通道商户密钥配置
///
/// 提供通道商户维度的 Jeepay 密钥配置管理。
/// Jeepay 无集测环境, 每通道商户仅一条配置(无环境维度参数)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Jeepay通道商户密钥配置")
@RestController
@RequestMapping("/admin/jeepay/key-config")
@RequiredArgsConstructor
public class JeepayKeyConfigController {

    private final JeepayKeyConfigService jeepayKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询Jeepay密钥配置")
    @GetMapping("/find-config")
    public Result<JeepayKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = jeepayKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setAppSecretConfigured(Objects.nonNull(config.getAppSecret()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存Jeepay密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated JeepayKeyConfigParam param) {
        jeepayKeyConfigService.save(param);
        return Res.ok();
    }
}
