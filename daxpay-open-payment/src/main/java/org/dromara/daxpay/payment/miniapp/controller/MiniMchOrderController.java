package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.miniapp.service.MiniMchOrderService;
import org.dromara.daxpay.payment.pay.param.order.refund.RefundCreateParam;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序订单操作
 * @author xxm
 * @since 2025/4/22
 */
@Validated
@ClientCode(DaxPayCode.Client.MERCHANT)
@Tag(name = "小程序订单操作")
@RestController
@RequestMapping("/mini/mch/order")
@RequestGroup(groupCode = "MiniMchOrder", groupName = "小程序订单操作", moduleCode = "MiniMch")
@RequiredArgsConstructor
public class MiniMchOrderController {
    private final MiniMchOrderService miniAppOrderService;

    @RequestPath("同步支付订单状态")
    @Operation(summary = "同步支付订单状态")
    @PostMapping("/syncPayOrder")
    public Result<Void> syncPayOrder(Long id) {
        miniAppOrderService.syncPayOrder(id);
        return Res.ok();
    }

    @RequestPath("关闭支付订单")
    @Operation(summary = "关闭支付订单")
    @PostMapping("/closePayOrder")
    public Result<Void> closePayOrder(@NotNull(message = "订单ID不可为空") Long id) {
        miniAppOrderService.closePayOrder(id);
        return Res.ok();
    }

    @RequestPath("撤销支付订单")
    @Operation(summary = "撤销支付订单")
    @PostMapping("/cancelPayOrder")
    public Result<Void> cancelPayOrder(@NotNull(message = "订单ID不可为空") Long id) {
        miniAppOrderService.cancelPayOrder(id);
        return Res.ok();
    }

    @RequestPath("创建退款订单")
    @Operation(summary = "创建退款订单")
    @PostMapping("/createRefundOrder")
    public Result<RefundResult> createRefundOrder(@RequestBody @Validated RefundCreateParam param) {
        return Res.ok(miniAppOrderService.createRefundOrder(param));
    }

    @RequestPath("同步退款订单状态")
    @Operation(summary = "同步退款订单状态")
    @PostMapping("/syncRefundOrder")
    public Result<Void> syncRefundOrder(@NotNull(message = "订单ID不可为空") Long id) {
        miniAppOrderService.syncRefundOrder(id);
        return Res.ok();
    }

    @RequestPath("重试退款订单")
    @Operation(summary = "重试退款订单")
    @PostMapping("/retryRefundOrder")
    public Result<Void> retryRefundOrder(@NotNull(message = "订单ID不可为空") Long id) {
        miniAppOrderService.retryRefundOrder(id);
        return Res.ok();
    }

    @RequestPath("关闭退款订单")
    @Operation(summary = "关闭退款订单")
    @PostMapping("/closeRefundOrder")
    public Result<Void> closeRefundOrder(@NotNull(message = "订单ID不可为空") Long id) {
        miniAppOrderService.closeRefundOrder(id);
        return Res.ok();
    }
}
