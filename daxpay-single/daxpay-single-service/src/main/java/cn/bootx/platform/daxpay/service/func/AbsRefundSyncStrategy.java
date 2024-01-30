package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import lombok.Getter;

/**
 * 支付退款订单同步策略
 * @author xxm
 * @since 2024/1/25
 */
@Getter
public abstract class AbsRefundSyncStrategy implements PayStrategy{

    private PayRefundOrder refundOrder;

    /**
     * 初始化参数
     */
    public void initRefundParam(PayRefundOrder refundOrder){
        this.refundOrder = refundOrder;
    }

    /**
     * 同步前处理, 主要是预防请求过于迅速, 支付网关没有处理完退款请求, 导致返回的状态不正确
     */
    public void doBeforeHandler(){}
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     * @see PaySyncStatusEnum
     */
    public abstract RefundGatewaySyncResult doSyncStatus();
}
