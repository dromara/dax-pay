package org.dromara.daxpay.service.controller.order;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.param.order.pay.PayOrderQuery;
import org.dromara.daxpay.service.result.order.pay.PayOrderVo;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.order.pay.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 支付订单控制器
 * @author xxm
 * @since 2024/1/9
 */
@Validated
@Tag(name = "支付订单控制器")
@RestController
@RequestMapping("/order/pay")
@RequestGroup(moduleCode = "TradeOrder", moduleName = "交易订单", groupCode = "PayOrder", groupName = "支付订单")
@RequiredArgsConstructor
public class PayOrderController {
    private final PayOrderQueryService queryService;
    private final PayOrderService payOrderService;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayOrderVo>> page(PageParam pageParam, PayOrderQuery param){
        return Res.ok(queryService.page(pageParam,param));
    }

    @RequestPath("查询订单详情")
    @Operation(summary = "查询订单详情")
    @GetMapping("/findById")
    public Result<PayOrderVo> findById(@NotNull(message = "支付订单id不能为空") Long id){
        PayOrderVo order = queryService.findById(id)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @RequestPath("根据订单号查询详情")
    @Operation(summary = "根据订单号查询详情")
    @GetMapping("/findByOrderNo")
    public Result<PayOrderVo> findByOrderNo(@NotBlank(message = "支付订单号不能为空") String orderNo){
        PayOrderVo order = queryService.findByOrderNo(orderNo)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<BigDecimal> getTotalAmount(PayOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }

    @RequestPath("同步支付订单状态")
    @Operation(summary = "同步支付订单状态")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "支付订单id不能为空") Long id){
        payOrderService.sync(id);
        return Res.ok();
    }

    @RequestPath("关闭支付订单")
    @Operation(summary = "关闭支付订单")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "支付订单id不能为空") Long id){
        payOrderService.close(id);
        return Res.ok();
    }

    @RequestPath("撤销支付订单")
    @Operation(summary = "撤销支付订单")
    @PostMapping("/cancel")
    public Result<Void> cancel(@NotNull(message = "支付订单id不能为空") Long id){
        payOrderService.cancel(id);
        return Res.ok();
    }
}
