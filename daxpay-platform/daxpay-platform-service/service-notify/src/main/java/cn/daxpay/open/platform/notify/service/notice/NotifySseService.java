package cn.daxpay.open.platform.notify.service.notice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

/// SSE 实时推送服务(管理在线用户的 SseEmitter)
///
/// 多连接方案: 以 userId 维护一组 emitter, 允许同一用户多标签页/多设备并存, 互不顶替;
/// 各 emitter 依靠自身生命周期回调(onCompletion/onTimeout/onError)与心跳发送失败自动清理.
/// 多实例横向扩展时需引入 Redis Pub/Sub 跨实例广播(预留扩展点).
@Slf4j
@Service
public class NotifySseService {

    /// userId -> 该用户的全部在线连接(支持多标签页/多设备)
    private final Map<Long, Set<SseEmitter>> emitters = new ConcurrentHashMap<>();

    /// 建立连接(同一用户新连接不顶替旧连接, 各自独立存活)
    public SseEmitter connect(Long userId) {
        // 永不超时, 依靠心跳维持
        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(emitter);
        // 建连立即发首字节(注释行, 不触发前端 onmessage): 消除首个心跳周期内的响应体空窗,
        // 防止 CDN/代理等待 body 时按首字节超时掐断流(线上 EdgeOne 实测 20-40s 断流)
        // 此时 handler 尚未接管, send 由 ResponseBodyEmitter 早期发送机制排队, 返回后回放
        try {
            emitter.send(SseEmitter.event().comment("connected"));
        } catch (IOException e) {
            // 早期发送仅内存排队阶段, 极少失败; 一旦失败交由后续心跳 send 的既有移除路径清理
            log.debug("SSE 建连首字节发送失败, userId={}", userId, e);
        }
        // 连接结束/超时/出错时自动从集合移除, 空集合回收 key
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));
        return emitter;
    }

    /// 从某用户的连接集合中移除单个 emitter, 集合空则回收 key
    private void removeEmitter(Long userId, SseEmitter emitter) {
        Set<SseEmitter> set = emitters.get(userId);
        if (Objects.isNull(set)) {
            return;
        }
        set.remove(emitter);
        // 只在集合确实为空时移除, 避免与并发新增竞争
        if (set.isEmpty()) {
            emitters.remove(userId, set);
        }
    }

    /// 主动断开该用户的全部连接(预留, 如强制下线场景)
    public void disconnect(Long userId) {
        Set<SseEmitter> set = emitters.remove(userId);
        if (Objects.isNull(set)) {
            return;
        }
        for (SseEmitter emitter : set) {
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        }
    }

    /// 推送给所有在线用户(公告发布场景)
    public void publishToAll(Object payload) {
        if (emitters.isEmpty()) {
            return;
        }
        emitters.forEach((_, set) -> sendAll(set, payload));
    }

    /// 推送给指定用户(个人消息场景)
    public void publishToUser(Long userId, Object payload) {
        Set<SseEmitter> set = emitters.get(userId);
        if (Objects.nonNull(set)) {
            sendAll(set, payload);
        }
    }

    /// 向一组连接广播负载, 发送失败的逐个移除
    private void sendAll(Set<SseEmitter> set, Object payload) {
        for (SseEmitter emitter : set) {
            try {
                emitter.send(SseEmitter.event().data(payload));
            } catch (IOException e) {
                set.remove(emitter);
            }
        }
    }

    /// 心跳: 每 10 秒发注释行, 防止 Nginx/CDN/代理超时断开
    ///
    /// 间隔必须小于链路上最小的回源/空闲超时: 原 25s 间隔长于 EdgeOne 回源超时,
    /// 首个心跳未到达即被掐断(线上实测 20-40s 断流); 10s 对 15s/30s/60s 各档超时均留余量
    @Scheduled(fixedRate = 10_000)
    public void heartbeat() {
        if (emitters.isEmpty()) {
            return;
        }
        emitters.forEach((userId, set) -> {
            for (SseEmitter emitter : set) {
                try {
                    // 注释行不触发前端 onmessage, 仅保活
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                } catch (IOException e) {
                    set.remove(emitter);
                }
            }
        });
    }
}
