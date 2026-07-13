package cn.daxpay.open.channel.fuyou.controller.isv;

import cn.daxpay.open.channel.fuyou.convert.isv.FuyouIsvKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvKeyConfigResult;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvKeyConfigService;
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

/// # 富友服务商密钥配置
@PermCode(menuCode = PermCodes.Payment.Fuyou.MENU)
@Validated
@Tag(name = "富友服务商密钥配置")
@RestController
@RequestMapping("/admin/fuyou/isv-key-config")
@RequiredArgsConstructor
public class FuyouIsvKeyConfigController {

    private final FuyouIsvKeyConfigService fuyouIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "富友服务商查看", nameEn = "Fuyou ISV View")
    @Operation(summary = "查询富友服务商密钥配置")
    @GetMapping("/find-config")
    public Result<FuyouIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            boolean sandbox) {
        return Res.ok(FuyouIsvKeyConfigConvert.CONVERT.toResult(fuyouIsvKeyConfigService.findByProduct(product, sandbox)));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "富友服务商管理", nameEn = "Fuyou ISV Manage")
    @Operation(summary = "保存富友服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated FuyouIsvKeyConfigParam param) {
        fuyouIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
