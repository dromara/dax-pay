package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付退款策略
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsRefundStrategy implements PaymentStrategy{

    /** 退款订单 */
    private RefundOrder refundOrder = null;

    /**
     * 退款前对处理, 主要进行各种检查
     */
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款操作
     */
    public abstract RefundResultBo doRefundHandler();

}
