package cn.daxpay.open.payment.core.strategy.refund;

import cn.daxpay.open.payment.core.strategy.PaymentStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;

/// # 退款抽象策略
///
/// 策略为单例无状态, 运行时数据通过 [PayRefundOrder] 显式传递。
/// 与支付关闭/同步策略一致, 每次退款创建新退款订单, 策略不持有可变状态。
public abstract class AbsRefundStrategy implements PaymentStrategy {

    /// 退款前处理(各通道可选实现, 如校验/预处理)
    public void doBeforeRefund(PayRefundOrder refundOrder) {
    }

    /// 退款操作
    /// @param refundOrder 退款订单
    /// @return 退款结果(含映射后的退款状态)
    public abstract RefundResultBo doRefund(PayRefundOrder refundOrder);

}
