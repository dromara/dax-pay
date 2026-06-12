package org.dromara.daxpay.platform.capability.cache.notify.consumer;

import org.dromara.daxpay.platform.capability.cache.core.LocalCacheRegistry;
import org.dromara.daxpay.platform.capability.cache.notify.message.CacheInvalidationMessage;
import org.dromara.daxpay.platform.capability.cache.notify.message.CacheInvalidationType;
import org.dromara.daxpay.platform.capability.cache.notify.support.CacheTopicConstants;
import org.dromara.daxpay.platform.common.rocketmq.message.RocketmqMessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/// # 缓存失效消息消费者
///
/// 使用广播模式，每个节点都会收到消息，收到后删除本机本地缓存。
///
/// 设计要点：
/// - 只删除本地 L1 缓存，不操作 Redis L2
/// - 消费失败不影响主流程，依赖 L1 TTL 兜底
///
/// 为什么只删除 L1 不操作 Redis：
/// - Redis L2 已由源节点在 evict/clear 时删除
/// - 广播消息的目的是同步其他节点的本地缓存
/// - 如果所有节点都操作 Redis，会放大写压力
@Slf4j
@Component
@RocketMQMessageListener(
        topic = CacheTopicConstants.TOPIC,
        consumerGroup = CacheTopicConstants.CONSUMER_GROUP,
        selectorExpression = CacheTopicConstants.TAG_EVICT + " || " + CacheTopicConstants.TAG_CLEAR,
        messageModel = MessageModel.BROADCASTING
)
@RequiredArgsConstructor
public class CacheInvalidationConsumer implements RocketMQListener<MessageExt> {

    private final LocalCacheRegistry localCacheRegistry;

    @Override
    public void onMessage(MessageExt messageExt) {
        var message = RocketmqMessageConverter.parseBody(messageExt, CacheInvalidationMessage.class);

        String cacheName = message.getCacheName();
        try {
            CacheInvalidationType type = CacheInvalidationType.valueOf(message.getType());
            switch (type) {
                case EVICT -> {
                    String key = message.getKey();
                    this.localCacheRegistry.evict(cacheName, key);
                    log.debug("收到缓存失效消息，删除本地缓存: cacheName={}, key={}", cacheName, key);
                }
                case CLEAR -> {
                    this.localCacheRegistry.clear(cacheName);
                    log.debug("收到缓存清空消息，清空本地缓存: cacheName={}", cacheName);
                }
            }
        } catch (IllegalArgumentException e) {
            log.warn("收到未知的缓存失效类型: cacheName={}, type={}", cacheName, message.getType());
        }
    }
}
