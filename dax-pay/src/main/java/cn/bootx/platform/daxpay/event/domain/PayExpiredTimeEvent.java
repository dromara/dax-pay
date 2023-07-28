package cn.bootx.platform.daxpay.event.domain;

import cn.bootx.platform.daxpay.code.PaymentEventCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付超时事件
 * @author xxm
 * @since 2023/7/20
 */
@Data
@Accessors(chain = true)
public class PayExpiredTimeEvent implements PayEvent{

    /** 支付单ID */
    private Long paymentId;

    /** 业务单号 */
    private String businessId;

    /**
     * MQ队列名称
     */
    @Override
    public String getQueueName() {
        return PaymentEventCode.PAYMENT_EXPIRED_TIME;
    }
}
