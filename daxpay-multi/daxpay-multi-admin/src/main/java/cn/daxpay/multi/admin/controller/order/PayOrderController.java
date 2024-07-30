package cn.daxpay.multi.admin.controller.order;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.param.order.pay.PayOrderQuery;
import cn.daxpay.multi.service.result.order.pay.PayOrderVo;
import cn.daxpay.multi.service.service.order.pay.PayOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付订单控制器")
@RestController
@RequestMapping("/order/pay")
@RequestGroup(moduleCode = "TradeOrder", moduleName = "交易订单", groupCode = "PayOrder", groupName = "支付订单")
@RequiredArgsConstructor
public class PayOrderController {
    private final PayOrderQueryService queryService;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayOrderVo>> page(PageParam pageParam, PayOrderQuery param){
        return Res.ok(queryService.page(pageParam,param));
    }

    @RequestPath("查询订单详情")
    @Operation(summary = "查询订单详情")
    @GetMapping("/findById")
    public Result<PayOrderVo> findById(Long id){
        PayOrderVo order = queryService.findById(id)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @RequestPath("根据订单号查询详情")
    @Operation(summary = "根据订单号查询详情")
    @GetMapping("/findByOrderNo")
    public Result<PayOrderVo> findByOrderNo(String orderNo){
        PayOrderVo order = queryService.findByOrderNo(orderNo)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<Integer> getTotalAmount(PayOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
