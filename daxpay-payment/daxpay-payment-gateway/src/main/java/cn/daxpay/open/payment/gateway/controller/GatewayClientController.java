package cn.daxpay.open.payment.gateway.controller;

import cn.daxpay.open.payment.gateway.service.GatewayAggregatePayService;
import cn.daxpay.open.payment.gateway.service.GatewayOrderQueryService;
import cn.daxpay.open.payment.gateway.param.AggregateQrPayParam;
import cn.daxpay.open.payment.gateway.result.GatewayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 网关 H5 侧接口(无商户签名)
@IgnoreAuth
@IgnoreTenant
@Validated
@Tag(name = "网关支付(H5)")
@RestController
@RequestMapping("/client/gateway")
@RequiredArgsConstructor
public class GatewayClientController {

    private final GatewayOrderQueryService gatewayOrderQueryService;
    private final GatewayAggregatePayService gatewayAggregatePayService;

    @Operation(summary = "查询网关订单摘要")
    @GetMapping("/order")
    public Result<GatewayOrderResult> getOrder(
            @NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo) {
        return Res.ok(gatewayOrderQueryService.queryByOrderNoNotTenant(orderNo));
    }

    @Operation(summary = "聚合扫码发起支付")
    @PostMapping("/aggregate/pay")
    public Result<NormalPayResult> aggregatePay(@RequestBody @Validated AggregateQrPayParam param) {
        return Res.ok(gatewayAggregatePayService.aggregateQrPay(param));
    }
}
