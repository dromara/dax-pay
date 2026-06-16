package org.dromara.daxpay.payment.strategy.sync;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.old.pay.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/// # 支付退款订单同步策略
///
@Getter
@Setter
public abstract class AbsSyncRefundOrderStrategy implements PaymentStrategy {

    private RefundOrder refundOrder;

    /// 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
    public void doBeforeHandler(){}
    /// 查询通道网关方的退款订单状态信息
    public abstract RefundSyncResultBo doSync();
}
