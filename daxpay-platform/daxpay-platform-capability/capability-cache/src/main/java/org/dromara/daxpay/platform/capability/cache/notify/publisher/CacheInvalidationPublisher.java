package org.dromara.daxpay.platform.capability.cache.notify.publisher;

import org.dromara.daxpay.platform.capability.cache.notify.message.CacheInvalidationMessage;
import org.dromara.daxpay.platform.capability.cache.notify.message.CacheInvalidationType;
import org.dromara.daxpay.platform.capability.cache.notify.support.CacheTopicConstants;
import org.dromara.daxpay.platform.common.rocketmq.service.RocketmqTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 缓存失效通知发布者
///
/// 通过 RocketMQ 广播缓存失效消息，通知其他节点删除本地 L1 缓存。
///
/// 设计要点：
/// - 使用广播模式，每个节点都会收到消息
/// - 只广播删除/清空操作，不广播更新操作
@Slf4j
@RequiredArgsConstructor
public class CacheInvalidationPublisher {

    private final RocketmqTemplateService rocketmqTemplateService;

    /// 发布删除缓存消息
    ///
    /// @param cacheName 缓存名称
    /// @param key       缓存键
    public void publishEvict(String cacheName, Object key) {
        CacheInvalidationMessage message = new CacheInvalidationMessage();
        message.setCacheName(cacheName);
        message.setKey(String.valueOf(key));
        message.setType(CacheInvalidationType.EVICT.name());

        try {
            rocketmqTemplateService.send(CacheTopicConstants.TOPIC, CacheTopicConstants.TAG_EVICT, message);
            log.debug("发布缓存失效消息成功: cacheName={}, key={}", cacheName, key);
        } catch (Exception e) {
            log.error("发布缓存失效消息失败: cacheName={}, key={}, error={}", cacheName, key, e.getMessage(), e);
        }
    }

    /// 发布清空缓存消息
    ///
    /// @param cacheName 缓存名称
    public void publishClear(String cacheName) {
        CacheInvalidationMessage message = new CacheInvalidationMessage();
        message.setCacheName(cacheName);
        message.setType(CacheInvalidationType.CLEAR.name());

        try {
            rocketmqTemplateService.send(CacheTopicConstants.TOPIC, CacheTopicConstants.TAG_CLEAR, message);
            log.debug("发布缓存清空消息成功: cacheName={}", cacheName);
        } catch (Exception e) {
            log.error("发布缓存清空消息失败: cacheName={}, error={}", cacheName, e.getMessage(), e);
        }
    }
}


