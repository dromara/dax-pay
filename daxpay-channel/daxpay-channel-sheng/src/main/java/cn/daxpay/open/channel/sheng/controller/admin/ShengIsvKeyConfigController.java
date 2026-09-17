package cn.daxpay.open.channel.sheng.controller.admin;

import cn.daxpay.open.channel.sheng.param.ShengIsvKeyConfigParam;
import cn.daxpay.open.channel.sheng.result.ShengIsvKeyConfigResult;
import cn.daxpay.open.channel.sheng.service.config.ShengIsvKeyConfigService;
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

/// # 盛付通服务商密钥配置
///
/// 提供产品级全局的盛付通服务商密钥配置管理(平台为唯一服务商, 每产品一条)。
/// 盛付通无沙箱环境, 无环境维度参数。
@PermCode(menuCode = PermCodes.Payment.Isv.MENU)
@Validated
@Tag(name = "盛付通服务商密钥配置")
@RestController
@RequestMapping("/admin/sheng/isv-key-config")
@RequiredArgsConstructor
public class ShengIsvKeyConfigController {

    private final ShengIsvKeyConfigService shengIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据产品编码查询盛付通服务商密钥配置")
    @GetMapping("/find-config")
    public Result<ShengIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        var config = shengIsvKeyConfigService.findByProduct(product);
        var result = config.toResult();
        // 密钥是否已配置标志(脱敏值回显, 前端据此判断是否需要重新录入)
        result.setMerchantPrivateKeyConfigured(Objects.nonNull(config.getMerchantPrivateKey()));
        result.setShengpayPublicKeyConfigured(Objects.nonNull(config.getShengpayPublicKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存盛付通服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated ShengIsvKeyConfigParam param) {
        shengIsvKeyConfigService.save(param);
        return Res.ok();
    }
}
