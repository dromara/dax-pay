package cn.daxpay.single.service.func;

import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.sync.result.RefundSyncResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付退款订单同步策略
 * @author xxm
 * @since 2024/1/25
 */
@Getter
@Setter
public abstract class AbsRefundSyncStrategy implements PayStrategy{

    private RefundOrder refundOrder;

    /**
     * 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
     */
    public void doBeforeHandler(){}
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     * @see PaySyncStatusEnum
     */
    public abstract RefundSyncResult doSyncStatus();
}
