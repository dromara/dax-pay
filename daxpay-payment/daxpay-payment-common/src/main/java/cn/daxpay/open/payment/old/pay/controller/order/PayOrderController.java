package cn.daxpay.open.payment.old.pay.controller.order;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.param.order.pay.PayOrderQuery;
import cn.daxpay.open.payment.old.pay.result.order.pay.PayOrderExpandResult;
import cn.daxpay.open.payment.old.pay.result.order.pay.PayOrderVo;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderQueryService;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付订单控制器
///
@Validated
@Tag(name = "支付订单控制器")
@RestController
@RequestMapping("/order/pay")
@RequiredArgsConstructor
public class PayOrderController {
    private final PayOrderQueryService queryService;
    private final PayOrderService payOrderService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayOrderVo>> page(PageParam pageParam, PayOrderQuery param){
        return Res.ok(queryService.page(pageParam,param));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/get")
    public Result<PayOrderVo> findById(@NotNull(message = "{validation.field.payOrderId.notNull}") Long id){
        PayOrderVo order = queryService.findById(id)
                .map(PayOrder::toResult)
                // 订单: 支付订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.payOrderNotExist"));
        return Res.ok(order);
    }

    @Operation(summary = "查询订单扩展详情")
    @GetMapping("/get-expand-by-id")
    public Result<PayOrderExpandResult> findExpandById(Long id){
        return Res.ok(queryService.findExpandByById(id));
    }

    @Operation(summary = "根据订单号查询详情")
    @GetMapping("/get-by-order-no")
    public Result<PayOrderVo> findByOrderNo(@NotBlank(message = "{validation.field.orderNo.notBlank}") @Parameter(description = "支付订单号") String orderNo){
        PayOrderVo order = queryService.findByOrderNo(orderNo)
                .map(PayOrder::toResult)
                // 订单: 支付订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.payOrderNotExist"));
        return Res.ok(order);
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/get-total-amount")
    public Result<Long> getTotalAmount(PayOrderQuery query){
        return Res.ok(queryService.getTotalAmount(query));
    }

    @Operation(summary = "同步支付订单状态")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "{validation.field.payOrderId.notNull}") Long id){
        payOrderService.sync(id);
        return Res.ok();
    }

    @Operation(summary = "关闭支付订单")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "{validation.field.payOrderId.notNull}") Long id){
        payOrderService.close(id);
        return Res.ok();
    }

    @Operation(summary = "撤销支付订单")
    @PostMapping("/cancel")
    public Result<Void> cancel(@NotNull(message = "{validation.field.payOrderId.notNull}") Long id){
        payOrderService.cancel(id);
        return Res.ok();
    }
}

