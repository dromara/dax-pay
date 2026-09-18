package cn.daxpay.open.channel.easypay.controller.admin;

import cn.daxpay.open.channel.easypay.param.EasyPayKeyConfigParam;
import cn.daxpay.open.channel.easypay.result.EasyPayKeyConfigResult;
import cn.daxpay.open.channel.easypay.service.config.EasyPayKeyConfigService;
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

/// # 易支付通道商户密钥配置
///
/// 提供通道商户维度的易支付密钥配置管理。
/// 易支付无集测环境, 每通道商户仅一条配置(无环境维度参数)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "易支付通道商户密钥配置")
@RestController
@RequestMapping("/admin/easypay/key-config")
@RequiredArgsConstructor
public class EasyPayKeyConfigController {

    private final EasyPayKeyConfigService easyPayKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询易支付密钥配置")
    @GetMapping("/find-config")
    public Result<EasyPayKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = easyPayKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setPlatformPublicKeyConfigured(Objects.nonNull(config.getPlatformPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存易支付密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated EasyPayKeyConfigParam param) {
        easyPayKeyConfigService.save(param);
        return Res.ok();
    }
}
