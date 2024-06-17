package cn.daxpay.single.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.param.payment.refund.RefundSyncParam;
import cn.daxpay.single.result.sync.RefundSyncResult;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.order.refund.service.RefundOrderQueryService;
import cn.daxpay.single.service.core.order.refund.service.RefundOrderService;
import cn.daxpay.single.service.core.payment.sync.service.RefundSyncService;
import cn.daxpay.single.service.dto.order.refund.RefundOrderDto;
import cn.daxpay.single.service.param.order.PayOrderRefundParam;
import cn.daxpay.single.service.param.order.RefundOrderQuery;
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
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderService refundOrderService;
    private final RefundOrderQueryService queryService;
    private final RefundSyncService refundSyncService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<RefundOrderDto>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }


    @Operation(summary = "查询退款订单详情")
    @GetMapping("/findByRefundNo")
    public ResResult<RefundOrderDto> findByRefundNo(String refundNo){
        return Res.ok(queryService.findByRefundNo(refundNo));
    }
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<RefundOrderDto> findById(Long id){
        return Res.ok(queryService.findById(id));
    }


    @InitPaymentContext(PaymentApiCode.REFUND)
    @Operation(summary = "手动发起退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody PayOrderRefundParam param){
        refundOrderService.refund(param);
        return Res.ok();
    }

    @InitPaymentContext(PaymentApiCode.REFUND)
    @Operation(summary = "重新发起退款")
    @PostMapping("/resetRefund")
    public ResResult<Void> resetRefund(Long id){
        refundOrderService.resetRefund(id);
        return Res.ok();
    }

    @InitPaymentContext(PaymentApiCode.SYNC_REFUND)
    @Operation(summary = "退款同步")
    @PostMapping("/syncByRefundNo")
    public ResResult<RefundSyncResult> syncByRefundNo(String refundNo){
        RefundSyncParam refundSyncParam = new RefundSyncParam();
        refundSyncParam.setRefundNo(refundNo);
        return Res.ok(refundSyncService.sync(refundSyncParam));
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public ResResult<Integer> getTotalAmount(RefundOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
