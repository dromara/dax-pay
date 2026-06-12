package org.dromara.daxpay.payment.pay.controller.order;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import org.dromara.daxpay.payment.pay.param.order.refund.RefundCreateParam;
import org.dromara.daxpay.payment.pay.param.order.refund.RefundOrderQuery;
import org.dromara.daxpay.payment.pay.result.order.refund.RefundOrderVo;
import org.dromara.daxpay.payment.pay.service.order.refund.RefundOrderQueryService;
import org.dromara.daxpay.payment.pay.service.order.refund.RefundOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/// # 支付退款控制器
///
@Validated
@Tag(name = "退款订单控制器")
@RestController
@RequestMapping("/order/refund")
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderQueryService queryService;
    private final RefundOrderService refundOrderService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderVo>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @Operation(summary = "查询退款订单详情")
    @GetMapping("/get-by-refund-no")
    public Result<RefundOrderVo> findByRefundNo(@NotBlank(message = "{validation.field.refundNo.notBlank}") @Parameter(description = "退款号") String refundNo){
        return Res.ok(queryService.findByRefundNo(refundNo));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/get")
    public Result<RefundOrderVo> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(queryService.findById(id));
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/get-total-amount")
    public Result<BigDecimal> getTotalAmount(RefundOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }

    @Operation(summary = "发起退款")
    @PostMapping("/create")
    public Result<RefundResult> create(@Validated @RequestBody RefundCreateParam param){
        return Res.ok(refundOrderService.create(param));
    }

    @Operation(summary = "退款同步")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "{validation.field.id.notNull}") Long id){
        refundOrderService.sync(id);
        return Res.ok();
    }

    @Operation(summary = "退款重试")
    @PostMapping("/retry")
    public Result<Void> retry(@NotNull(message = "{validation.field.id.notNull}") Long id){
        refundOrderService.retry(id);
        return Res.ok();
    }

    @Operation(summary = "退款关闭")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "{validation.field.id.notNull}") Long id){
        refundOrderService.close(id);
        return Res.ok();
    }

}
