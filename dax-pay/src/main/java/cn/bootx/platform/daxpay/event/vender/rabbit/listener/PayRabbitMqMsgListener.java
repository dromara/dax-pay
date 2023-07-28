package cn.bootx.platform.daxpay.event.vender.rabbit.listener;

import cn.bootx.platform.daxpay.code.PaymentEventCode;
import cn.bootx.platform.daxpay.core.sync.service.PayExpiredTimeService;
import cn.bootx.platform.daxpay.event.domain.PayCancelEvent;
import cn.bootx.platform.daxpay.event.domain.PayCompleteEvent;
import cn.bootx.platform.daxpay.event.domain.PayRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 消息接收
 *
 * @author xxm
 * @since 2021/4/22
 */
@Slf4j
@Component
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rabbit")
@RequiredArgsConstructor
public class PayRabbitMqMsgListener {

    private final PayExpiredTimeService payExpiredTimeService;

    /**
     * 支付超时事件处理
     */
    @RabbitListener(queues = PaymentEventCode.PAYMENT_EXPIRED_TIME)
    public void PaymentExpiredTime(Long paymentId) {
        payExpiredTimeService.expiredTime(paymentId);
    }

    /**
     * 支付成功
     */
    @RabbitListener(queues = PaymentEventCode.PAY_COMPLETE)
    public void payCancel(PayCompleteEvent event) {
        log.info("支付完成事件:{}", event);
    }

    /**
     * 支付撤销/关闭
     */
    @RabbitListener(queues = PaymentEventCode.PAY_CANCEL)
    public void payCancel(PayCancelEvent event) {
        log.info("支付撤销/关闭事件:{}", event);
    }

    /**
     * 支付退款
     */
    @RabbitListener(queues = PaymentEventCode.PAY_REFUND)
    public void payCancel(PayRefundEvent event) {
        log.info("支付退款事件:{}", event);
    }

}
