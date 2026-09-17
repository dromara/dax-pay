package cn.daxpay.open.channel.sheng.controller.admin;

import cn.daxpay.open.channel.sheng.param.ShengKeyConfigParam;
import cn.daxpay.open.channel.sheng.result.ShengKeyConfigResult;
import cn.daxpay.open.channel.sheng.service.config.ShengKeyConfigService;
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

/// # 盛付通通道商户密钥配置
///
/// 提供通道商户维度的盛付通密钥配置管理。
/// 盛付通无沙箱环境, 每通道商户仅一条配置(无环境维度参数)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "盛付通通道商户密钥配置")
@RestController
@RequestMapping("/admin/sheng/key-config")
@RequiredArgsConstructor
public class ShengKeyConfigController {

    private final ShengKeyConfigService shengKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询盛付通密钥配置")
    @GetMapping("/find-config")
    public Result<ShengKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = shengKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setShengpayPublicKeyConfigured(Objects.nonNull(config.getShengpayPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存盛付通密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated ShengKeyConfigParam param) {
        shengKeyConfigService.save(param);
        return Res.ok();
    }
}
