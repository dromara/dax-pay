package cn.daxpay.open.channel.lakala.controller.isv;

import cn.daxpay.open.channel.lakala.convert.isv.LakalaIsvKeyConfigConvert;
import cn.daxpay.open.channel.lakala.param.isv.LakalaIsvKeyConfigParam;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvKeyConfigResult;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvKeyConfigService;
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

/// # 拉卡拉服务商密钥配置
///
@PermCode(menuCode = PermCodes.Payment.Lakala.MENU)
@Validated
@Tag(name = "拉卡拉服务商密钥配置")
@RestController
@RequestMapping("/admin/lakala/isv-key-config")
@RequiredArgsConstructor
public class LakalaIsvKeyConfigController {

    private final LakalaIsvKeyConfigService lakalaIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "拉卡拉服务商查看", nameEn = "Lakala ISV View")
    @Operation(summary = "查询拉卡拉服务商密钥配置")
    @GetMapping("/find-config")
    public Result<LakalaIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            boolean sandbox) {
        return Res.ok(LakalaIsvKeyConfigConvert.CONVERT.toResult(lakalaIsvKeyConfigService.findByProduct(product, sandbox)));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "拉卡拉服务商管理", nameEn = "Lakala ISV Manage")
    @Operation(summary = "保存拉卡拉服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated LakalaIsvKeyConfigParam param) {
        lakalaIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
