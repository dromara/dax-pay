package org.dromara.daxpay.payment.pay.strategy;

import org.dromara.daxpay.payment.pay.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账同步策略
 * @author xxm
 * @since 2024/7/25
 */
@Getter
@Setter
public abstract class AbsSyncTransferOrderStrategy implements PaymentStrategy{

    private TransferOrder transferOrder;

    /**
     * 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
     */
    public void doBeforeHandler(){}
    /**
     * 查询通道网关方的退款订单状态信息
     */
    public abstract TransferSyncResultBo doSync();
}
