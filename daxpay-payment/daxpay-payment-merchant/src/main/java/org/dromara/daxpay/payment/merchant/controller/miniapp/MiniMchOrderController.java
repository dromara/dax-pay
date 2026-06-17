package org.dromara.daxpay.payment.merchant.controller.miniapp;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.old.pay.service.order.pay.PayOrderService;
import org.dromara.daxpay.payment.old.pay.service.order.refund.RefundOrderService;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import org.dromara.daxpay.payment.old.pay.param.order.refund.RefundCreateParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 小程序订单操作
///
@Validated
@Tag(name = "小程序订单操作")
@RestController
@RequestMapping("/mini/mch/order")
@RequiredArgsConstructor
public class MiniMchOrderController {
    private final PayOrderService payOrderService;
    private final RefundOrderService refundOrderService;
    @Operation(summary = "同步支付订单状态")
    @PostMapping("/sync-pay-order")
    public Result<Void> syncPayOrder(Long id) {
        payOrderService.sync(id);
        return Res.ok();
    }

    @Operation(summary = "关闭支付订单")
    @PostMapping("/close-pay-order")
    public Result<Void> closePayOrder(@NotNull(message = "{validation.field.orderId.notNull}") Long id) {
        payOrderService.close(id);
        return Res.ok();
    }

    @Operation(summary = "撤销支付订单")
    @PostMapping("/cancel-pay-order")
    public Result<Void> cancelPayOrder(@NotNull(message = "{validation.field.orderId.notNull}") Long id) {
        payOrderService.cancel(id);
        return Res.ok();
    }

    @Operation(summary = "创建退款订单")
    @PostMapping("/create-refund-order")
    public Result<RefundResult> createRefundOrder(@RequestBody @Validated RefundCreateParam param) {
        return Res.ok(refundOrderService.create(param));
    }

    @Operation(summary = "同步退款订单状态")
    @PostMapping("/sync-refund-order")
    public Result<Void> syncRefundOrder(@NotNull(message = "{validation.field.orderId.notNull}") Long id) {
        refundOrderService.sync(id);
        return Res.ok();
    }

    @Operation(summary = "重试退款订单")
    @PostMapping("/retry-refund-order")
    public Result<Void> retryRefundOrder(@NotNull(message = "{validation.field.orderId.notNull}") Long id) {
        refundOrderService.retry(id);
        return Res.ok();
    }

    @Operation(summary = "关闭退款订单")
    @PostMapping("/close-refund-order")
    public Result<Void> closeRefundOrder(@NotNull(message = "{validation.field.orderId.notNull}") Long id) {
        refundOrderService.close(id);
        return Res.ok();
    }
}
