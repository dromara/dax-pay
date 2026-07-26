package cn.daxpay.open.payment.app.merchant.controller.gateway;

import cn.daxpay.open.payment.app.merchant.service.gateway.AppMerchantGatewayAggregateService;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
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

/// # 网关聚合扫码配置(商户移动端)
///
/// 面向商户移动端的聚合支付配置。业务编排委托 [AppMerchantGatewayAggregateService]；
/// 写操作强制当前上下文 mchNo，并通过 MchAppInfoService 校验应用归属。
@PermCode(menuCode = PermCodes.Merchant.GatewayAggregate.MENU)
@Validated
@Tag(name = "网关聚合扫码配置(商户移动端)")
@RestController
@RequestMapping("/app-merchant/gateway/aggregate-config")
@RequiredArgsConstructor
public class AppMerchantGatewayAggregateController {

    private final AppMerchantGatewayAggregateService gatewayAggregateService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用查询聚合扫码配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayAggregateConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(gatewayAggregateService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新聚合扫码配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayAggregateConfigParam param) {
        gatewayAggregateService.saveOrUpdate(param);
        return Res.ok();
    }
}
