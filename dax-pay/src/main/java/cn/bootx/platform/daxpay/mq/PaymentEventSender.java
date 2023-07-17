package cn.bootx.platform.daxpay.mq;

import cn.bootx.platform.daxpay.code.PaymentEventCode;
import cn.bootx.platform.daxpay.mq.event.PayCancelEvent;
import cn.bootx.platform.daxpay.mq.event.PayCompleteEvent;
import cn.bootx.platform.daxpay.mq.event.PayRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 支付中心消息发送器
 *
 * @author xxm
 * @since 2021/4/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventSender {

    private final PayMqMsgSender mqMsgSender;

    /**
     * 支付完成 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayComplete(PayCompleteEvent event) {
        mqMsgSender.send(event);
    }

    /**
     * 支付撤销/关闭 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayCancel(PayCancelEvent event) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAY_CANCEL, event);
    }

    /**
     * 支付退款 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayRefund(PayRefundEvent event) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAY_REFUND, event);
    }

    /**
     * 支付单超时 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPaymentExpiredTime(Long paymentId) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAYMENT_EXPIRED_TIME,
                paymentId);
    }

}
