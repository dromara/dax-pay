package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileQueryService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.service.PayReconcileService;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileOrderDto;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileDetailQuery;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileOrderCreate;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付对账
 * @author xxm
 * @since 2024/1/18
 */
@Tag(name = "支付对账控制器")
@RestController
@RequestMapping("/order/reconcile")
@RequiredArgsConstructor
public class PayReconcileOrderController {
    private final PayReconcileService reconcileService;
    private final PayReconcileQueryService reconcileQueryService;

    @Operation(summary = "手动创建支付对账订单")
    @PostMapping("/create")
    public ResResult<Void> create(@RequestBody ReconcileOrderCreate param){
        ValidationUtil.validateParam(param);
        reconcileService.create(param.getDate(), param.getChannel());
        return Res.ok();
    }

    @Operation(summary = "手动触发对账文件下载")
    @PostMapping("/downAndSave")
    public ResResult<Void> downAndSave(Long id){
        reconcileService.downAndSave(id);
        return Res.ok();
    }

    @Operation(summary = "手动触发对账单比对")
    @PostMapping("/compare")
    public ResResult<Void> compare(Long id){
        reconcileService.compare(id);
        return Res.ok();
    }

    @Operation(summary = "订单分页")
    @GetMapping("/page")
    public ResResult<PageResult<PayReconcileOrderDto>> page(PageParam pageParam, ReconcileOrderQuery query){
        return Res.ok(reconcileQueryService.page(pageParam, query));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/findById")
    public ResResult<PayReconcileOrderDto> findById(Long id){
        return Res.ok(reconcileQueryService.findById(id));
    }

    @Operation(summary = "对账明细分页")
    @GetMapping("/pageDetail")
    public ResResult<PageResult<PayReconcileDetailDto>> pageDetail(PageParam pageParam, ReconcileDetailQuery query){
        return Res.ok(reconcileQueryService.pageDetail(pageParam, query));
    }

    @Operation(summary = "对账明细详情")
    @GetMapping("/findDetailById")
    public ResResult<PayReconcileDetailDto> findDetailById(Long id){
        return Res.ok(reconcileQueryService.findDetailById(id));
    }
}
