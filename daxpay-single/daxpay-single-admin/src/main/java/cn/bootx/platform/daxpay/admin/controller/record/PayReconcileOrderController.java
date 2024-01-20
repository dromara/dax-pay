package cn.bootx.platform.daxpay.admin.controller.record;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.service.PayReconcileService;
import cn.bootx.platform.daxpay.service.param.reconcile.CreateReconcileOrderParam;
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
@RequestMapping("/record/reconcile")
@RequiredArgsConstructor
public class PayReconcileOrderController {
    private final PayReconcileService reconcileService;
    private final PayReconcileOrderService reconcileOrderService;

    @Operation(summary = "手动创建支付对账订单")
    @PostMapping("/createOrder")
    public ResResult<Void> createOrder(@RequestBody CreateReconcileOrderParam param){
        ValidationUtil.validateParam(param);
        reconcileOrderService.create(param.getDate(), param.getChannel());
        return Res.ok();
    }

    @Operation(summary = "手动触发对账文件下载")
    @PostMapping("/downById")
    public ResResult<Void> downByChannel(Long id){
        reconcileService.downAndSave(id);
        return Res.ok();
    }
}
