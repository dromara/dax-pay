package cn.bootx.platform.daxpay.admin.controller.record;

import cn.bootx.platform.daxpay.service.core.payment.reconcile.service.PayReconcileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付对账
 * @author xxm
 * @since 2024/1/18
 */
@Tag(name = "支付对账控制器")
@RestController
@RequestMapping("/record/reconcile")
@RequiredArgsConstructor
public class PayReconcileRecordController {
    private final PayReconcileService reconcileService;
}
