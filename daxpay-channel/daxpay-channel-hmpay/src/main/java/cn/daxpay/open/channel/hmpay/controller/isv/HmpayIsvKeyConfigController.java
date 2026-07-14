package cn.daxpay.open.channel.hmpay.controller.isv;

import cn.daxpay.open.channel.hmpay.convert.isv.HmpayIsvKeyConfigConvert;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvKeyConfigParam;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvKeyConfigResult;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvKeyConfigService;
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

/// # 河马付服务商密钥配置
@PermCode(menuCode = PermCodes.Payment.Isv.MENU)
@Validated
@Tag(name = "河马付服务商密钥配置")
@RestController
@RequestMapping("/admin/hmpay/isv-key-config")
@RequiredArgsConstructor
public class HmpayIsvKeyConfigController {

    private final HmpayIsvKeyConfigService hmpayIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询河马付服务商密钥配置")
    @GetMapping("/find-config")
    public Result<HmpayIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            boolean sandbox) {
        return Res.ok(HmpayIsvKeyConfigConvert.CONVERT.toResult(hmpayIsvKeyConfigService.findByProduct(product, sandbox)));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存河马付服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated HmpayIsvKeyConfigParam param) {
        hmpayIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
