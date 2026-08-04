package cn.daxpay.open.channel.adapay.controller.isv;

import cn.daxpay.open.channel.adapay.convert.isv.AdapayIsvKeyConfigConvert;
import cn.daxpay.open.channel.adapay.param.isv.AdapayIsvKeyConfigParam;
import cn.daxpay.open.channel.adapay.result.isv.AdapayIsvKeyConfigResult;
import cn.daxpay.open.channel.adapay.service.isv.AdapayIsvKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Adapay 服务商密钥配置
///
/// 平台为唯一服务商, 同一环境(生产/沙箱)仅一条配置, 与通道商户直连密钥配置并存。
@PermCode(menuCode = PermCodes.Payment.Isv.MENU)
@Validated
@Tag(name = "Adapay 服务商密钥配置")
@RestController
@RequestMapping("/admin/adapay/isv-key-config")
@RequiredArgsConstructor
public class AdapayIsvKeyConfigController {

    private final AdapayIsvKeyConfigService adapayIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询Adapay 服务商密钥配置")
    @GetMapping("/find-config")
    public Result<AdapayIsvKeyConfigResult> findConfig(
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        return Res.ok(AdapayIsvKeyConfigConvert.CONVERT.toResult(adapayIsvKeyConfigService.findBySandbox(sandbox)));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存Adapay 服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated AdapayIsvKeyConfigParam param) {
        adapayIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
