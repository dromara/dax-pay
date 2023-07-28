package cn.bootx.platform.daxpay.event.vender.rocket.sender;

import cn.bootx.platform.daxpay.event.PayMqMsgSender;
import cn.bootx.platform.daxpay.event.domain.PayEvent;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * rocketMQ 发送器
 * @author xxm
 * @since 2023/7/17
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rocket")
public class PayRocketMqMsgSender implements PayMqMsgSender {
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 实时推送MQ消息
     */
    @Override
    public void send(PayEvent msg) {
        rocketMQTemplate.syncSend(msg.getQueueName(),msg.toMessage());
    }

    /**
     * 推送MQ延迟消息
     */
    @Override
    public void send(PayEvent msg, int delay) {
        rocketMQTemplate.syncSend(msg.getQueueName(),msg.toMessage(),delay);
    }
}
