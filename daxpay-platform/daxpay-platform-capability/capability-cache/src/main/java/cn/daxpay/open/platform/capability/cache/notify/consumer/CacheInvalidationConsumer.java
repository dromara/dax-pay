package cn.daxpay.open.platform.capability.cache.notify.consumer;

import cn.daxpay.open.platform.capability.cache.core.LocalCacheRegistry;
import cn.daxpay.open.platform.capability.cache.notify.message.CacheInvalidationMessage;
import cn.daxpay.open.platform.capability.cache.notify.message.CacheInvalidationType;
import cn.daxpay.open.platform.capability.cache.notify.support.CacheTopicConstants;
import cn.daxpay.open.platform.common.artemis.ArtemisBeanNames;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 缓存失效消息消费者
///
/// 使用 JMS Topic（pub-sub）订阅，每个节点都会收到消息，收到后删除本机本地缓存。
///
/// 设计要点：
/// - 只删除本地 L1 缓存，不操作 Redis L2
/// - 消费失败不影响主流程，依赖 L1 TTL 兜底
/// - non-durable 订阅：消息可丢，节点重启后 L1 会重建，无需补收离线期间消息
///
/// 为什么只删除 L1 不操作 Redis：
/// - Redis L2 已由源节点在 evict/clear 时删除
/// - 广播消息的目的是同步其他节点的本地缓存
/// - 如果所有节点都操作 Redis，会放大写压力
///
/// 部署约束：
/// - broker 端 `cache-invalidation-topic` 地址必须配置为 multicast 路由类型，否则广播失效
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheInvalidationConsumer {

    private final LocalCacheRegistry localCacheRegistry;

    /// 订阅缓存失效 Topic
    ///
    /// 通过通用的 `topicListenerFactory`（pubSubDomain=true）配置 Topic 模式，
    /// 每个 non-durable 订阅者都会收到消息，实现跨节点广播。
    /// 方法签名接收原始 JSON 字符串（统一 Text 传输），手动反序列化为目标类型。
    @JmsListener(
            destination = CacheTopicConstants.TOPIC,
            containerFactory = ArtemisBeanNames.TOPIC_LISTENER_FACTORY
    )
    public void onMessage(String json) {
        CacheInvalidationMessage message;
        try {
            message = JacksonUtil.toBean(json, CacheInvalidationMessage.class);
        } catch (Exception e) {
            log.warn("缓存失效消息解析失败，忽略: json={}, error={}", json, e.getMessage());
            return;
        }

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
