package cn.daxpay.open.payment.core.strategy.sync;

import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.PaymentStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;

/// # 支付同步抽象类
///
/// 策略为单例无状态，运行时数据通过 [PayStrategyContext] 显式传递。
/// 上下文同时携带容器(业务单)与资金凭证，策略无需再自查容器表。
public abstract class AbsSyncPayOrderStrategy implements PaymentStrategy {

    /// 查询通道网关方的支付订单状态信息
    public abstract PaySyncResultBo doSync(PayStrategyContext context);

}
