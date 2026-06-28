package cn.daxpay.open.payment.strategy.sync;

import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.payment.pay.bo.PaySyncResultBo;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;

/// # 支付同步抽象类
///
/// 策略为单例无状态，运行时数据通过方法参数显式传递。
public abstract class AbsSyncPayOrderStrategy implements PaymentStrategy {

    /// 查询通道网关方的退款订单状态信息
    public abstract PaySyncResultBo doSync(PayTrade trade);

}
