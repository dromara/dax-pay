package cn.bootx.platform.daxpay.event.vender.rabbit.sender;

import cn.bootx.platform.daxpay.event.PayMqMsgSender;
import cn.bootx.platform.daxpay.event.domain.PayEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static cn.bootx.platform.daxpay.code.PaymentEventCode.DELAYED_EXCHANGE_PAYMENT;

/**
 * RabbitMQ消息发送器
 * @author xxm
 * @since 2023/7/17
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rabbit")
public class PayRabbitMqMsgSender implements PayMqMsgSender {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 实时推送MQ消息
     */
    @Override
    public void send(PayEvent msg) {
        rabbitTemplate.convertAndSend(msg.getQueueName(), msg.toMessage());
    }

    /**
     * 推送MQ延迟消息
     */
    @Override
    public void send(PayEvent msg, int delay) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_PAYMENT, msg.getQueueName(), msg.toMessage(),
                messagePostProcessor ->{
                    messagePostProcessor.getMessageProperties().setDelay(Math.toIntExact(delay * 1000L));
                    return messagePostProcessor;
                });
    }
}
