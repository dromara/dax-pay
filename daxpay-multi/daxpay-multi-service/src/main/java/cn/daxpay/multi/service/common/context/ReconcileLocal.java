package cn.daxpay.multi.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付对账上下文信息
 * @author xxm
 * @since 2024/1/21
 */
@Data
@Accessors(chain = true)
public class ReconcileLocal {

    /** 对账订单 */
//    private ReconcileOrder reconcileOrder;

    /** 通用支付对账记录 */
//    private List<ReconcileOutTrade> reconcileTradeDetails;

}
