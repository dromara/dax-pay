package cn.bootx.platform.common.websocket.manager;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * websocket管理器 (Spring封装的socket) 用于管理用户链接
 *
 * @author xxm
 * @since 2022/5/27
 */
@Getter
public class SpringWebSocketSessionManager {

    // session缓存
    private final Map<String, WebSocketSession> sessionPool = new ConcurrentSkipListMap<>();

    // sessionId 与 用户标识id 的映射关系 n:1
    private final Map<String, String> sid2uid = new ConcurrentSkipListMap<>();

    // 用户标识id 与 sessionId 的映射关系 1:n
    private final Map<String, List<String>> uid2sid = new ConcurrentSkipListMap<>();

    /**
     * 添加会话session关联
     */
    public void addSession(String userId, WebSocketSession session) {
        try {
            sid2uid.put(session.getId(), userId);
            sessionPool.put(session.getId(), session);
            List<String> list = Optional.ofNullable(uid2sid.get(userId)).orElse(new CopyOnWriteArrayList<>());
            list.add(session.getId());
            uid2sid.put(userId, list);
        }
        catch (Exception ignored) {
        }
    }

    /**
     * 删掉 连接Session
     */
    public void removeSession(WebSocketSession session) {
        sessionPool.remove(session.getId());
        String id = sid2uid.remove(session.getId());
        Optional.ofNullable(uid2sid.get(id)).ifPresent(list -> list.removeIf(s -> Objects.equals(s, session.getId())));
    }

    /**
     * 删除
     */
    public void removeSessionById(String id) {
        List<String> sessionIds = Optional.ofNullable(uid2sid.get(id)).orElse(Lists.newArrayList());
        sessionIds.forEach(sessionPool::remove);
        sessionIds.forEach(sid2uid::remove);
        uid2sid.remove(id);

    }

    /**
     * 根据userId获取关联的session列表
     */
    public List<WebSocketSession> getSessionsByUserId(String userId) {
        List<String> sessionIds = Optional.ofNullable(uid2sid.get(userId)).orElse(Lists.newArrayList());
        return sessionIds.stream().map(sessionPool::get).collect(Collectors.toList());
    }

    /**
     * 获取所有连接session
     */
    public List<WebSocketSession> getSessions() {
        return ListUtil.toList(sessionPool.values());
    }

    /**
     * 根据session获取连接id
     */
    public String getIdBySession(WebSocketSession session) {
        return sid2uid.get(session.getId());
    }

    /**
     * 根据session获取连接id
     */
    public String getIdBySessionId(String sessionId) {
        return sid2uid.get(sessionId);
    }

}
