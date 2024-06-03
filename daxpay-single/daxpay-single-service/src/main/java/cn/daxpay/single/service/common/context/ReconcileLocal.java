package cn.daxpay.single.service.common.context;

import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOutTrade;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
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

    /** 对账订单 */
    private ReconcileOrder reconcileOrder;

    /** 通用支付对账记录 */
    private List<ReconcileOutTrade> reconcileTradeDetails;

}
