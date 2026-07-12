package cn.daxpay.open.payment.core.strategy.refund;

import cn.daxpay.open.payment.core.strategy.PaymentStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;

/// # 退款同步抽象策略
///
/// 策略为单例无状态, 运行时数据通过 [PayRefundOrder] 显式传递。
/// 用于查询通道网关方的退款最终状态。
public abstract class AbsSyncRefundStrategy implements PaymentStrategy {

    /// 查询通道网关方的退款状态
    /// @param refundOrder 退款订单
    /// @return 退款同步结果(含映射后的退款状态)
    public abstract RefundResultBo doSync(PayRefundOrder refundOrder);

}
