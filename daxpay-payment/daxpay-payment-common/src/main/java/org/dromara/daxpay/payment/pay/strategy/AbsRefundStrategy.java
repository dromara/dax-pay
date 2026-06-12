package org.dromara.daxpay.payment.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.pay.bo.trade.RefundResultBo;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/// # 抽象支付退款策略
///
@Getter
@Setter
public abstract class AbsRefundStrategy implements PaymentStrategy {

    /// 退款订单
    private RefundOrder refundOrder = null;

    /// 退款前对处理, 主要进行各种检查
    public void doBeforeRefundHandler() {
    }

    /// 退款操作
    public abstract RefundResultBo doRefundHandler();

}
