package org.dromara.daxpay.payment.unipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.result.gateway.CheckoutCounterOrderAndConfigResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.service.gateway.CheckoutCounterPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关收银支付服务(收银台/码牌)
 * @author xxm
 * @since 2025/4/12
 */
@Validated
@IgnoreAuth
@Tag(name = "网关收银台支付服务")
@RestController
@RequestMapping("/unipay/gateway/checkout")
@RequiredArgsConstructor
public class GatewayCheckoutController {
    private final CheckoutCounterPayService checkoutCounterPayService;


    @Operation(summary = "获取配置和支付订单信息(收银台)")
    @GetMapping("/getOrderAndConfig")
    public Result<CheckoutCounterOrderAndConfigResult> getOrderAndConfig(
            @NotBlank(message = "订单号不能为空") String orderNo,
            @NotBlank(message = "收银台类型不能为空") String type){
        return Res.ok(checkoutCounterPayService.getOrderAndConfig(orderNo,  type));
    }

    @Operation(summary = "发起支付(收银台)")
    @PostMapping("/pay")
    public Result<PayResult> pay( @NotBlank(message = "订单号不能为空") String orderNo,
                                  @NotBlank(message = "收银台配置ID") Long configId){
        return Res.ok(checkoutCounterPayService.cashierPay(orderNo, configId));
    }
}
