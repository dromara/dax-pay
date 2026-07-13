package cn.daxpay.open.payment.admin.controller.merchant.gateway;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayAggregateConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 网关聚合扫码配置(管理)
@PermCode(menuCode = PermCodes.Merchant.GatewayAggregate.MENU)
@Validated
@Tag(name = "网关聚合扫码配置")
@RestController
@RequestMapping("/admin/gateway/aggregate-config")
@RequiredArgsConstructor
public class GatewayAggregateConfigAdminController {

    private final GatewayAggregateConfigService gatewayAggregateConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "配置查看", nameEn = "Config View")
    @Operation(summary = "按应用查询聚合扫码配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayAggregateConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(gatewayAggregateConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.UPDATE, nameCn = "配置更新", nameEn = "Config Update")
    @Operation(summary = "保存或更新聚合扫码配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayAggregateConfigParam param) {
        gatewayAggregateConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
