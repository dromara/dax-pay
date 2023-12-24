package cn.bootx.platform.daxpay.openapi.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.daxpay.annotation.PaymentApi;
import cn.bootx.platform.daxpay.core.payment.pay.service.PayService;
import cn.bootx.platform.daxpay.param.pay.*;
import cn.bootx.platform.daxpay.result.DaxResult;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.result.pay.RefundResult;
import cn.bootx.platform.daxpay.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一支付接口
 * @author xxm
 * @since 2023/12/15
 */
@IgnoreAuth
@Tag(name = "统一支付接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {
    private final PayService payService;

    @PaymentApi("pay")
    @Operation(summary = "统一下单")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        payService.pay(payParam);
        return DaxRes.ok();
    }

    @PaymentApi("simplePay")
    @Operation(summary = "简单下单")
    @PostMapping("/simplePay")
    public DaxResult<PayResult> simplePay(){
        return DaxRes.ok();
    }

    @PaymentApi("cancel")
    @Operation(summary = "订单撤销")
    @PostMapping("/cancel")
    public DaxResult<Void> cancel(@RequestBody CancelParam param){
        return DaxRes.ok();
    }

    @PaymentApi("close")
    @Operation(summary = "订单关闭")
    @PostMapping("/close")
    public DaxResult<Void> close(@RequestBody CloseParam param){
        return DaxRes.ok();
    }

    @PaymentApi("refund")
    @Operation(summary = "统一退款")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam param){
        return DaxRes.ok();
    }

    @PaymentApi("simpleRefund")
    @Operation(summary = "简单退款")
    @PostMapping("/simpleRefund")
    public DaxResult<RefundResult> simpleRefund(@RequestBody SimpleRefundParam param){
        return DaxRes.ok();
    }

    @PaymentApi("syncPay")
    @Operation(summary = "支付状态同步")
    @PostMapping("/syncPay")
    public DaxResult<Void> syncPay(){
        return DaxRes.ok();
    }

    @PaymentApi("syncRefund")
    @Operation(summary = "退款状态同步")
    @PostMapping("/syncRefund")
    public DaxResult<Void> syncRefund(@RequestBody RefundSyncParam param){
        return DaxRes.ok();
    }
}
