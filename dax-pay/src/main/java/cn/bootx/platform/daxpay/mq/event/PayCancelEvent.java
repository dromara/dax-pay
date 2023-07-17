package cn.bootx.platform.daxpay.mq.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付撤销事件
 *
 * @author xxm
 * @since 2022/7/11
 */
@Data
@Accessors(chain = true)
public class PayCancelEvent implements PayEvent{

    /** 支付单ID */
    private Long paymentId;

    /** 业务单号 */
    private String businessId;

    /**
     * MQ队列名称
     */
    @Override
    public String getQueueName() {
        return null;
    }

}
