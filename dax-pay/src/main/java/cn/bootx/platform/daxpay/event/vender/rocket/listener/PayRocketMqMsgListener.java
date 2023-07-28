package cn.bootx.platform.daxpay.event.vender.rocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * rocketMQ 消息监听
 * @author xxm
 * @since 2023/7/17
 */
@Slf4j
@Component
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rocket")
//@RocketMQMessageListener(topic ="MQ_NAME", consumerGroup = "MQ_NAME")
public class PayRocketMqMsgListener implements RocketMQListener<String> {

    /**
     * 消息处理
     */
    @Override
    public void onMessage(String message) {

    }
}
