package cn.daxpay.open.channel.dougong.controller.isv;

import cn.daxpay.open.channel.dougong.convert.isv.DougongIsvKeyConfigConvert;
import cn.daxpay.open.channel.dougong.param.isv.DougongIsvKeyConfigParam;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvKeyConfigResult;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvKeyConfigService;
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

/// # 斗拱服务商密钥配置
@PermCode(menuCode = PermCodes.Payment.Isv.MENU)
@Validated
@Tag(name = "斗拱服务商密钥配置")
@RestController
@RequestMapping("/admin/dougong/isv-key-config")
@RequiredArgsConstructor
public class DougongIsvKeyConfigController {

    private final DougongIsvKeyConfigService dougongIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Payment.Isv.VIEW_NAME_CN, nameEn = PermCodes.Payment.Isv.VIEW_NAME_EN)
    @Operation(summary = "查询斗拱服务商密钥配置")
    @GetMapping("/find-config")
    public Result<DougongIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(DougongIsvKeyConfigConvert.CONVERT.toResult(dougongIsvKeyConfigService.findByProduct(product)));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Payment.Isv.MANAGE_NAME_CN, nameEn = PermCodes.Payment.Isv.MANAGE_NAME_EN)
    @Operation(summary = "保存斗拱服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated DougongIsvKeyConfigParam param) {
        dougongIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
