package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import lombok.Getter;

/**
 * 支付退款修复策略
 * @author xxm
 * @since 2024/1/25
 */
@Getter
public abstract class AbsRefundRepairStrategy implements PayStrategy{
    private PayRefundOrder refundOrder;
    private PayRefundChannelOrder refundChannelOrder;
    private PayOrder payOrder;
    private PayChannelOrder payChannelOrder;

    /**
     * 初始化参数
     */
    public void initParam(PayRefundOrder refundOrder,
                          PayRefundChannelOrder refundChannelOrder,
                          PayOrder payOrder,
                          PayChannelOrder payChannelOrder){
        this.refundOrder = refundOrder;
        this.refundChannelOrder = refundChannelOrder;
        this.payOrder = payOrder;
        this.payChannelOrder = payChannelOrder;
    }


    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    public abstract RefundGatewaySyncResult doSyncStatus();

}
