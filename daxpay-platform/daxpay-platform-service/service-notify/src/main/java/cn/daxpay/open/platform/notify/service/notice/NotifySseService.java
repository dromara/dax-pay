package cn.daxpay.open.platform.notify.service.notice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
        // 连接结束/超时/出错时自动从集合移除, 空集合回收 key
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));
        return emitter;
    }

    /// 从某用户的连接集合中移除单个 emitter, 集合空则回收 key
    private void removeEmitter(Long userId, SseEmitter emitter) {
        Set<SseEmitter> set = emitters.get(userId);
        if (set == null) {
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
        if (set == null) {
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
        emitters.forEach((userId, set) -> sendAll(set, payload));
    }

    /// 推送给指定用户(个人消息场景)
    public void publishToUser(Long userId, Object payload) {
        Set<SseEmitter> set = emitters.get(userId);
        if (set != null) {
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

    /// 心跳: 每 25 秒发注释行, 防止 Nginx/代理超时断开
    @Scheduled(fixedRate = 25_000)
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
