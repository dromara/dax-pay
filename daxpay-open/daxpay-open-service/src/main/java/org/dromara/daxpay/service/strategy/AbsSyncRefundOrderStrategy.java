package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付退款订单同步策略
 * @author xxm
 * @since 2024/1/25
 */
@Getter
@Setter
public abstract class AbsSyncRefundOrderStrategy implements PaymentStrategy{

    private RefundOrder refundOrder;

    /**
     * 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
     */
    public void doBeforeHandler(){}
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    public abstract RefundSyncResultBo doSync();
}
