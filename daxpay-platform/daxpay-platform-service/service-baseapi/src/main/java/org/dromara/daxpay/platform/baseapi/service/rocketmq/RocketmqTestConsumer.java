package org.dromara.daxpay.platform.baseapi.service.rocketmq;

import org.dromara.daxpay.platform.baseapi.entity.rocketmq.RocketmqTestMessage;
import org.dromara.daxpay.platform.common.rocketmq.message.RocketmqMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/// # RocketMQ测试消费者
///
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "test-topic",
        consumerGroup = "test-consumer-group",
        selectorExpression = "test-tag || delay-tag"
)
public class RocketmqTestConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        String businessId = messageExt.getKeys();
        RocketmqTestMessage body = RocketmqMessageConverter.parseBody(messageExt, RocketmqTestMessage.class);
        log.info("RocketMQ消费消息: topic={}, tag={}, businessId={}, body={}",
                messageExt.getTopic(),
                messageExt.getTags(),
                businessId,
                body);
    }
}
