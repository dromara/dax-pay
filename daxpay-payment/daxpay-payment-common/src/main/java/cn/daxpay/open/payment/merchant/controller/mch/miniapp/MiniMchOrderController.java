package cn.daxpay.open.payment.merchant.controller.mch.miniapp;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderService;
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

}
