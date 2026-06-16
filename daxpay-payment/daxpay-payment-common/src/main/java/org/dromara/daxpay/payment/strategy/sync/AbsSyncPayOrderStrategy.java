package org.dromara.daxpay.payment.strategy.sync;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.pay.bo.PaySyncResultBo;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import lombok.Getter;
import lombok.Setter;

/// # 支付同步抽象类
///
@Getter
@Setter
public abstract class AbsSyncPayOrderStrategy implements PaymentStrategy {

    /// 资金交易凭证
    private PayTrade trade = null;

    /// 查询通道网关方的退款订单状态信息
    public abstract PaySyncResultBo doSync();

}
