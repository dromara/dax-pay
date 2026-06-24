package cn.daxpay.open.platform.notify.service.notice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// SSE 实时推送服务(管理在线用户的 SseEmitter)
///
/// 单实例方案: 以 userId 维护本地 emitter 映射; 公告发布时推送给所有在线用户.
/// 多实例横向扩展时需引入 Redis Pub/Sub 跨实例广播(预留扩展点).
@Slf4j
@Service
public class NotifySseService {

    /// userId -> SseEmitter
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /// 建立连接
    public SseEmitter connect(Long userId) {
        // 顶掉旧连接
        SseEmitter old = emitters.remove(userId);
        if (old != null) {
            try {
                old.complete();
            } catch (Exception ignored) {
            }
        }
        // 0L 表示不超时, 依靠心跳维持
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId, emitter));
        emitter.onTimeout(() -> emitters.remove(userId, emitter));
        emitter.onError(e -> emitters.remove(userId, emitter));
        return emitter;
    }

    /// 主动断开
    public void disconnect(Long userId) {
        SseEmitter emitter = emitters.remove(userId);
        if (emitter != null) {
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
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().data(payload));
            } catch (IOException e) {
                emitters.remove(userId, emitter);
            }
        });
    }

    /// 推送给指定用户(个人消息场景, 预留)
    public void publishToUser(Long userId, Object payload) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) {
            return;
        }
        try {
            emitter.send(SseEmitter.event().data(payload));
        } catch (IOException e) {
            emitters.remove(userId, emitter);
        }
    }

    /// 心跳: 每 25 秒发注释行, 防止 Nginx/代理超时断开
    @Scheduled(fixedRate = 25_000)
    public void heartbeat() {
        if (emitters.isEmpty()) {
            return;
        }
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (IOException e) {
                emitters.remove(userId, emitter);
            }
        });
    }
}
