package cn.bootx.platform.daxpay.core.pay.builder;

import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.mq.event.*;
import lombok.experimental.UtilityClass;

/**
 * 支付事件生成器
 *
 * @author xxm
 * @since 2022/7/11
 */
@UtilityClass
public class PayEventBuilder {

    /**
     * 支付完成
     */
    public PayCompleteEvent buildPayComplete(Payment payment) {
        return new PayCompleteEvent().setPaymentId(payment.getId())
            .setBusinessId(payment.getBusinessId());
    }

    /**
     * 支付撤销/关闭
     */
    public PayCancelEvent buildPayCancel(Payment payment) {
        return new PayCancelEvent().setPaymentId(payment.getId())
            .setBusinessId(payment.getBusinessId());
    }

    /**
     * 支付超时
     */
    public PayExpiredTimeEvent buildPayExpiredTime(Payment payment) {
        return new PayExpiredTimeEvent().setPaymentId(payment.getId())
            .setBusinessId(payment.getBusinessId());
    }

    /**
     * 支付退款
     */
    public PayRefundEvent buildPayRefund(Payment payment) {
        return new PayRefundEvent().setPaymentId(payment.getId())
            .setBusinessId(payment.getBusinessId());
    }

}
