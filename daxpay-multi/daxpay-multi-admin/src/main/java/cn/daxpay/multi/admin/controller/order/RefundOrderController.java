package cn.daxpay.multi.admin.controller.order;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.service.param.order.refund.RefundOrderQuery;
import cn.daxpay.multi.service.result.order.refund.RefundOrderResult;
import cn.daxpay.multi.service.service.order.refund.RefundOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付退款控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "退款订单控制器")
@RestController
@RequestMapping("/order/refund")
@RequestGroup(moduleCode = "TradeOrder", groupCode = "RefundOrder", groupName = "退款订单")
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderQueryService queryService;

    @RequestPath("退款订单分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<RefundOrderResult>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }


    @RequestPath("根据商户退款号查询")
    @Operation(summary = "查询退款订单详情")
    @GetMapping("/findByRefundNo")
    public Result<RefundOrderResult> findByRefundNo(String refundNo){
        return Res.ok(queryService.findByRefundNo(refundNo));
    }

    @RequestPath("根据ID查询")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<RefundOrderResult> findById(Long id){
        return Res.ok(queryService.findById(id));
    }


    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<Integer> getTotalAmount(RefundOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
