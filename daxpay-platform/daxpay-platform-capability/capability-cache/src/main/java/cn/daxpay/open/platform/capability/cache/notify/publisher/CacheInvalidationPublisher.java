package cn.daxpay.open.platform.capability.cache.notify.publisher;

import cn.daxpay.open.platform.capability.cache.notify.message.CacheInvalidationMessage;
import cn.daxpay.open.platform.capability.cache.notify.message.CacheInvalidationType;
import cn.daxpay.open.platform.capability.cache.notify.support.CacheTopicConstants;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 缓存失效通知发布者
///
/// 通过 Artemis 广播缓存失效消息，通知其他节点删除本地 L1 缓存。
///
/// 设计要点：
/// - 使用 Topic（multicast 路由）广播，每个节点都会收到消息
/// - 只广播删除/清空操作，不广播更新操作
///
/// 部署约束：
/// - broker 端 `cache-invalidation-topic` 地址必须配置为 multicast 路由类型
/// - 消费端每个节点用独立的 durable subscription 名（见 `CacheInvalidationConsumer`）
@Slf4j
@RequiredArgsConstructor
public class CacheInvalidationPublisher {

    private final ArtemisTemplateService artemisTemplateService;

    /// 发布删除缓存消息
    ///
    /// @param cacheName 缓存名称
    /// @param key       缓存键
    public void publishEvict(String cacheName, Object key) {
        var message = new CacheInvalidationMessage();
        message.setCacheName(cacheName);
        message.setKey(String.valueOf(key));
        message.setType(CacheInvalidationType.EVICT.name());

        try {
            // 缓存失效是广播语义，必须走 sendTopic（broker 端 cache-invalidation-topic 为 multicast 路由）
            artemisTemplateService.sendTopic(
                    CacheTopicConstants.TOPIC, JacksonUtil.toJson(message, false));
            log.debug("发布缓存失效消息成功: cacheName={}, key={}", cacheName, key);
        } catch (Exception e) {
            log.error("发布缓存失效消息失败: cacheName={}, key={}, error={}", cacheName, key, e.getMessage(), e);
        }
    }

    /// 发布清空缓存消息
    ///
    /// @param cacheName 缓存名称
    public void publishClear(String cacheName) {
        var message = new CacheInvalidationMessage();
        message.setCacheName(cacheName);
        message.setType(CacheInvalidationType.CLEAR.name());

        try {
            // 缓存清空也是广播语义，同样使用 sendTopic
            artemisTemplateService.sendTopic(
                    CacheTopicConstants.TOPIC, JacksonUtil.toJson(message, false));
            log.debug("发布缓存清空消息成功: cacheName={}", cacheName);
        } catch (Exception e) {
            log.error("发布缓存清空消息失败: cacheName={}, error={}", cacheName, e.getMessage(), e);
        }
    }
}
