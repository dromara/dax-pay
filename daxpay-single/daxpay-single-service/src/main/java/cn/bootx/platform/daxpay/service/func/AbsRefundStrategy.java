package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 抽象支付退款策略
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsRefundStrategy implements PayStrategy{

    /** 支付订单 */
    private PayOrder payOrder = null;

    /** 退款订单 */
    private RefundOrder refundOrder = null;


    /**
     * 退款前对处理, 主要进行配置的加载和检查
     */
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款操作
     */
    public abstract void doRefundHandler();

    /**
     * 退款发起成功操作
     */
    public void doSuccessHandler() {
        // 更新退款订单数据状态和完成时间
        this.getRefundChannelOrder()
                .setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setRefundTime(LocalDateTime.now());

        // 支付通道订单可退余额
        int refundableBalance = this.getPayChannelOrder()
                .getRefundableBalance() - this.getRefundChannelOrder().getAmount();
        // 如果可退金额为0说明已经全部退款
        PayStatusEnum status = refundableBalance == 0 ? PayStatusEnum.REFUNDED : PayStatusEnum.PARTIAL_REFUND;
        this.getPayChannelOrder()
                .setRefundableBalance(refundableBalance)
                .setStatus(status.getCode());
    }

}
