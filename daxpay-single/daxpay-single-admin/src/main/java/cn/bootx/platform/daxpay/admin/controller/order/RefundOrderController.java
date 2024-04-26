package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.param.payment.refund.RefundSyncParam;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.service.core.order.refund.service.RefundOrderQueryService;
import cn.bootx.platform.daxpay.service.core.order.refund.service.RefundOrderService;
import cn.bootx.platform.daxpay.service.core.payment.refund.service.RefundService;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.RefundSyncService;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderExtraDto;
import cn.bootx.platform.daxpay.service.param.order.PayOrderRefundParam;
import cn.bootx.platform.daxpay.service.param.order.RefundOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付退款控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付退款控制器")
@RestController
@RequestMapping("/order/refund")
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderService refundOrderService;
    private final RefundOrderQueryService refundOrderQueryService;
    private final RefundSyncService refundSyncService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<RefundOrderDto>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(refundOrderQueryService.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<RefundOrderDto> findById(Long id){
        return Res.ok(refundOrderQueryService.findById(id));
    }

    @Operation(summary = "查询扩展信息")
    @GetMapping("/findExtraById")
    public ResResult<RefundOrderExtraDto> findExtraById(Long id){
        return Res.ok(refundOrderQueryService.findExtraById(id));
    }

    @Operation(summary = "手动发起退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody PayOrderRefundParam param){
        refundOrderService.refund(param);
        return Res.ok();
    }

    @Operation(summary = "退款同步")
    @PostMapping("/syncById")
    public ResResult<SyncResult> syncById(String refundNo){
        RefundSyncParam refundSyncParam = new RefundSyncParam();
        refundSyncParam.setRefundNo(refundNo);
        return Res.ok(refundSyncService.sync(refundSyncParam));
    }
}
