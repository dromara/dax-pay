package cn.daxpay.open.plugin.easypay.controller.config;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayConfigParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayConfigResult;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 易支付场景配置
///
@Validated
@Tag(name = "易支付场景配置")
@RestController
@RequestMapping({"/admin/easypay/config", "/merchant/easypay/config"})
@RequiredArgsConstructor
public class EasyPayConfigController {

    private final EasyPayConfigService easyPayConfigService;

    @Operation(summary = "按pid查询配置")
    @GetMapping("/get-by-pid")
    public Result<EasyPayConfigResult> getByPid(
            @NotNull(message = "{validation.field.pid.notNull}") Integer pid) {
        return Res.ok(easyPayConfigService.findByPid(pid).toResult());
    }

    @Operation(summary = "更新配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated EasyPayConfigParam param) {
        easyPayConfigService.update(param);
        return Res.ok();
    }
}
