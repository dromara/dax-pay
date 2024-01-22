package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 支付对账上下文信息
 * @author xxm
 * @since 2024/1/21
 */
@Data
@Accessors(chain = true)
public class ReconcileLocal {

    /** 通用支付对账记录 */
    private List<PayReconcileDetail> reconcileDetails;


}
