package org.dromara.daxpay.service.controller.order;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.order.refund.RefundCreateParam;
import org.dromara.daxpay.service.param.order.refund.RefundOrderQuery;
import org.dromara.daxpay.service.result.order.refund.RefundOrderVo;
import org.dromara.daxpay.service.service.order.refund.RefundOrderQueryService;
import org.dromara.daxpay.service.service.order.refund.RefundOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 支付退款控制器
 * @author xxm
 * @since 2024/1/9
 */
@Validated
@Tag(name = "退款订单控制器")
@RestController
@RequestMapping("/order/refund")
@RequestGroup(moduleCode = "TradeOrder", groupCode = "RefundOrder", groupName = "退款订单")
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderQueryService queryService;
    private final RefundOrderService refundOrderService;

    @RequestPath("退款订单分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderVo>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @RequestPath("根据商户退款号查询")
    @Operation(summary = "查询退款订单详情")
    @GetMapping("/findByRefundNo")
    public Result<RefundOrderVo> findByRefundNo(@NotBlank(message = "退款号不可为空") String refundNo){
        return Res.ok(queryService.findByRefundNo(refundNo));
    }

    @RequestPath("根据ID查询")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<RefundOrderVo> findById(@NotNull(message = "退款ID不可为空") Long id){
        return Res.ok(queryService.findById(id));
    }

    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<BigDecimal> getTotalAmount(RefundOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }

    @RequestPath("发起退款")
    @Operation(summary = "发起退款")
    @PostMapping("/create")
    public Result<Void> create(@Validated @RequestBody RefundCreateParam param){
        refundOrderService.create(param);
        return Res.ok();
    }

    @RequestPath("退款同步")
    @Operation(summary = "退款同步")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "退款ID不可为空") Long id){
        refundOrderService.sync(id);
        return Res.ok();
    }

    @RequestPath("退款重试")
    @Operation(summary = "退款重试")
    @PostMapping("/retry")
    public Result<Void> retry(@NotNull(message = "退款ID不可为空") Long id){
        refundOrderService.retry(id);
        return Res.ok();
    }

    @RequestPath("退款关闭")
    @Operation(summary = "退款关闭")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "退款ID不可为空") Long id){
        refundOrderService.close(id);
        return Res.ok();
    }

}
