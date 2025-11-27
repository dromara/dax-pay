package org.dromara.daxpay.payment.unipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.pay.result.gateway.AggregateOrderAndConfigResult;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.payment.unipay.param.gateway.*;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.service.gateway.AggregateQrPayService;
import org.dromara.daxpay.payment.unipay.service.gateway.GatewayPayAssistService;
import org.dromara.daxpay.payment.unipay.service.gateway.GatewayPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网关支付服务
 * @author xxm
 * @since 2024/11/26
 */
@Validated
@IgnoreAuth
@Tag(name = "网关支付服务")
@RestController
@RequestMapping("/unipay/gateway")
@RequiredArgsConstructor
public class GatewayPayController {
    private final GatewayPayService gatewayPayService;
    private final GatewayPayAssistService payAssistService;
    private final AggregateQrPayService aggregateQrPayService;

    @PaymentVerify
    @Operation(summary = "创建一个聚合扫码支付链接")
    @PostMapping("/prePay")
    public DaxResult<GatewayPayUrlResult> prePay(@RequestBody GatewayPayParam checkoutParam){
        return DaxRes.ok(gatewayPayService.prePay(checkoutParam));
    }

    @PaymentVerify
    @Operation(summary = "发起支付(聚合付款码)")
    @PostMapping("/barPay")
    public DaxResult<PayResult> aggregateBarPay(@RequestBody AggregateBarPayParam param){
        return DaxRes.ok(gatewayPayService.aggregateBarPay(param));
    }

    @Operation(summary = "发起支付(聚合扫码)")
    @PostMapping("/qrPay")
    public Result<PayResult> qrPay(@RequestBody @Validated AggregateQrPayParam param){
        return Res.ok(gatewayPayService.aggregateQrPay(param));
    }

    @Operation(summary = "获取支付配置和支付订单信息(聚合扫码)")
    @GetMapping("/getQrPayConfig")
    public Result<AggregateOrderAndConfigResult> getQrPayConfig(
            @NotBlank(message = "订单号不能为空") @Parameter(description = "订单号") String orderNo,
            @NotBlank(message = "支付场景不能为空") @Parameter(description = "支付场景")String scene){
        return Res.ok(aggregateQrPayService.getQrPayConfig(orderNo, scene));
    }

    @Operation(summary = "获取用于获取OpenId信息的链接(聚合扫码)")
    @PostMapping("/generateAuthUrl")
    public Result<String> generateAuthUrl(
            @NotBlank(message = "订单号不能为空") @Parameter(description = "订单号") String orderNo,
            @NotBlank(message = "支付场景不能为空") @Parameter(description = "支付场景")String scene){
        return Res.ok(aggregateQrPayService.genAuthUrl(orderNo, scene));
    }

    @Operation(summary = "获取授权结果(聚合扫码)")
    @PostMapping("/auth")
    public Result<AuthResult> auth(@RequestBody CheckoutCounterAuthCodeParam param){
        ValidationUtil.validateParam(param);
        return Res.ok(aggregateQrPayService.auth(param));
    }

    @Operation(summary = "查询订单状态(通用)")
    @GetMapping("/findStatusByOrderNo")
    public Result<Boolean> findStatusByOrderNo(@NotBlank(message = "订单号不能为空") String orderNo){
        return Res.ok(payAssistService.findStatusByOrderNo(orderNo));
    }

    @Operation(summary = "查询订单信息(通用)")
    @GetMapping("/findOrderByOrderNo")
    public Result<GatewayOrderResult> findOrderByOrderNo(@NotBlank(message = "订单号不能为空") String orderNo){
        return Res.ok(payAssistService.findOrderByOrderNo(orderNo));
    }
}
