package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import lombok.Getter;

import java.util.Objects;

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
    public void initRepairParam(PayRefundOrder refundOrder,
                                PayRefundChannelOrder refundChannelOrder,
                                PayOrder payOrder,
                                PayChannelOrder payChannelOrder){
        this.refundOrder = refundOrder;
        this.refundChannelOrder = refundChannelOrder;
        this.payOrder = payOrder;
        this.payChannelOrder = payChannelOrder;
    }


    /**
     * 退款成功修复
     */
    public void doSuccessHandler(){
        PayChannelOrder payChannelOrder = this.getPayChannelOrder();
        PayRefundChannelOrder refundChannelOrder = this.getRefundChannelOrder();

        // 判断是全部退款还是部分退款
        if (Objects.equals(payChannelOrder.getRefundableBalance(), 0)){
            //全部退款
            payChannelOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            // 部分退款
            payChannelOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());

        }
        refundChannelOrder.setStatus(PayRefundStatusEnum.SUCCESS.getCode());
    }

    /**
     * 退款失败, 关闭退款订单
     */
    public void doCloseHandler(){
        PayChannelOrder payChannelOrder = this.getPayChannelOrder();
        PayRefundChannelOrder refundChannelOrder = this.getRefundChannelOrder();
        int refundableBalance = payChannelOrder.getRefundableBalance() + payChannelOrder.getAmount();
        payChannelOrder.setRefundableBalance(refundableBalance);
        // 判断是支付完成还是部分退款
        if (Objects.equals(payChannelOrder.getRefundableBalance(), payChannelOrder.getAmount())){
            // 全部退款
            payChannelOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
        } else {
            // 部分退款
            payChannelOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());

        }
        // 如果失败, 可退余额设置为null
        refundChannelOrder.setRefundableAmount(null);
        refundChannelOrder.setStatus(PayRefundStatusEnum.CLOSE.getCode());
    }

}
