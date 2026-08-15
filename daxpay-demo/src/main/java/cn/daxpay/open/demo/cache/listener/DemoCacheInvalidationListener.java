package cn.daxpay.open.demo.cache.listener;

import cn.daxpay.open.demo.cache.result.CacheInvalidationEventResult;
import cn.daxpay.open.platform.capability.cache.notify.message.CacheInvalidationMessage;
import cn.daxpay.open.platform.capability.cache.notify.support.CacheTopicConstants;
import cn.daxpay.open.platform.common.artemis.ArtemisBeanNames;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/// # 缓存失效广播观察者(演示)
///
/// 与平台缓存失效消费者 [cn.daxpay.open.platform.capability.cache.notify.consumer.CacheInvalidationConsumer]
/// 订阅同一个 Artemis multicast Topic, 作为并列的第二个订阅者——只记录事件、不删缓存,
/// 把「本节点收到了集群失效广播」暴露给演示页面。
///
/// 多节点语义: multicast Topic 下每个节点、每个订阅者都会收到同一条消息。
/// 单机演示看到的一条事件即本节点收到的通知(发送节点自己也在订阅者列表中);
/// 多节点部署时每个节点的 CacheInvalidationConsumer 都会各自删除自己的 L1, 实现集群 L1 一致性。
@Slf4j
@Component
public class DemoCacheInvalidationListener {

    /// 事件保留条数(环形裁剪, 演示用足够)
    private static final int MAX_EVENTS = 50;

    /// 失效事件环形队列(新的在队头)
    private final Deque<CacheInvalidationEventResult> events = new ConcurrentLinkedDeque<>();

    /// 订阅缓存失效 Topic(与 CacheInvalidationConsumer 并列的独立订阅, 消息互不影响)
    ///
    /// 方法签名接收原始 JSON 字符串(统一 Text 传输), 手动反序列化为目标类型
    @JmsListener(
            destination = CacheTopicConstants.TOPIC,
            containerFactory = ArtemisBeanNames.TOPIC_LISTENER_FACTORY
    )
    public void onMessage(String json) {
        CacheInvalidationMessage message;
        try {
            message = JacksonUtil.toBean(json, CacheInvalidationMessage.class);
        } catch (Exception e) {
            log.warn("缓存失效消息解析失败, 忽略: json={}, error={}", json, e.getMessage());
            return;
        }
        var event = new CacheInvalidationEventResult()
                .setTime(OffsetDateTime.now())
                .setType(message.getType())
                .setCacheName(message.getCacheName())
                .setKey(message.getKey());
        synchronized (this) {
            this.events.addFirst(event);
            while (this.events.size() > MAX_EVENTS) {
                this.events.removeLast();
            }
        }
        log.info("演示观察者收到缓存失效广播: type={}, cacheName={}, key={}",
                message.getType(), message.getCacheName(), message.getKey());
    }

    /// 最近失效事件列表(新的在前)
    public List<CacheInvalidationEventResult> recentEvents() {
        return List.copyOf(this.events);
    }
}
