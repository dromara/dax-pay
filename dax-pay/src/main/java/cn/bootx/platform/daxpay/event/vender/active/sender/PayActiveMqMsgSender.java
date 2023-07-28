package cn.bootx.platform.daxpay.event.vender.active.sender;

import cn.bootx.platform.daxpay.event.PayMqMsgSender;
import cn.bootx.platform.daxpay.event.domain.PayEvent;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * activeMQ 发送器
 * @author xxm
 * @since 2023/7/20
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "active")
public class PayActiveMqMsgSender implements PayMqMsgSender {

    private final JmsTemplate jmsTemplate;

    /**
     * 实时推送MQ消息
     */
    @Override
    public void send(PayEvent msg) {
        jmsTemplate.convertAndSend(new ActiveMQQueue(msg.getQueueName()),msg.toMessage());
    }

    /**
     * 推送MQ延迟消息
     */
    @Override
    public void send(PayEvent msg, int delay) {
        jmsTemplate.send(new ActiveMQQueue(msg.getQueueName()), session -> {
            TextMessage tm = session.createTextMessage(msg.toMessage().toString());
            tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay * 1000);
            tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
            tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
            return tm;
        });
    }
}
