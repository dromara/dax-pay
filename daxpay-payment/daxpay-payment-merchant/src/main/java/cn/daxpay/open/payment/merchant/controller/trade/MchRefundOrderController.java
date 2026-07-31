package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.service.trade.MchRefundOrderService;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 退款订单(商户端)
///
/// 业务编排委托 [MchRefundOrderService]；强制当前商户过滤。
@PermCode(menuCode = PermCodes.Trade.Refund.MENU)
@Validated
@Tag(name = "退款订单(商户端)")
@RestController
@RequestMapping("/mch/order/refund")
@RequiredArgsConstructor
public class MchRefundOrderController {

    private final MchRefundOrderService mchRefundOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "退款订单分页")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderResult>> page(PageParam pageParam, RefundOrderQuery query) {
        return Res.ok(mchRefundOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询退款订单详情")
    @GetMapping("/get-by-id")
    public Result<RefundOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchRefundOrderService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起退款")
    @PostMapping("/refund")
    public Result<RefundOrderResult> refund(@Valid @RequestBody RefundParam param) {
        return Res.ok(mchRefundOrderService.refund(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步退款状态")
    @PostMapping("/sync")
    public Result<RefundOrderResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchRefundOrderService.sync(id));
    }
}
