package org.dromara.daxpay.payment.old.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.old.pay.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import lombok.Getter;
import lombok.Setter;

/// # 支付同步抽象类
///
@Getter
@Setter
public abstract class AbsSyncPayOrderStrategy implements PaymentStrategy {

    /// 支付订单
    private PayOrder order = null;

    /// 查询通道网关方的退款订单状态信息
    public abstract PaySyncResultBo doSync();

}
