package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantRefundOrderService;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 退款订单(商户移动端)
///
/// 面向商户移动端的退款订单查询。业务编排委托 [AppMerchantRefundOrderService]。
@PermCode(menuCode = PermCodes.Trade.Refund.MENU)
@Validated
@Tag(name = "退款订单(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/refund")
@RequiredArgsConstructor
public class AppMerchantRefundOrderController {

    private final AppMerchantRefundOrderService refundOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "退款订单分页")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderResult>> page(PageParam pageParam, RefundOrderQuery query) {
        return Res.ok(refundOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询退款订单详情")
    @GetMapping("/get-by-id")
    public Result<RefundOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(refundOrderService.findById(id));
    }
}
