package cn.bootx.platform.common.websocket.manager;

import cn.hutool.core.collection.ListUtil;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * websocket管理器 (java原生, 进攻演示, 通常使用 SpringWebSocketSessionManager )
 *
 * @author xxm
 * @since 2022/5/27
 */
public class WebSocketSessionManager {

    // session缓存
    protected static final Map<String, Session> sessionPool = new ConcurrentHashMap<>();

    // sessionId 与 用户标识id 的映射关系 n:1
    protected static final Map<String, String> sid2uid = new ConcurrentHashMap<>();

    // 用户标识id 与 sessionId 的映射关系 1:n
    protected static final Map<String, List<String>> uid2sid = new ConcurrentHashMap<>();

    /**
     * 添加会话session关联
     */
    public void addSession(String id, Session session) {
        try {
            sid2uid.put(session.getId(), id);
            sessionPool.put(session.getId(), session);
            List<String> list = Optional.ofNullable(uid2sid.get(id)).orElse(new CopyOnWriteArrayList<>());
            list.add(session.getId());
            uid2sid.put(id, list);
        }
        catch (Exception ignored) {
        }
    }

    /**
     * 删掉 连接Session
     */
    public void removeSession(Session session) {
        sessionPool.remove(session.getId());
        String id = sid2uid.remove(session.getId());
        Optional.ofNullable(uid2sid.get(id)).ifPresent(list -> list.removeIf(s -> Objects.equals(s, session.getId())));
    }

    /**
     * 删除
     */
    public void removeSessionById(String id) {
        List<String> sessionIds = uid2sid.get(id);
        sessionIds.forEach(sessionPool::remove);
        sessionIds.forEach(sid2uid::remove);
        uid2sid.remove(id);

    }

    /**
     * 根据id获取关联的session列表
     */
    public List<Session> getSessionsById(String id) {
        List<String> sessionIds = uid2sid.get(id);
        return sessionIds.stream().map(sessionPool::get).collect(Collectors.toList());
    }

    /**
     * 获取所有连接session
     */
    public List<Session> getSessions() {
        return ListUtil.toList(sessionPool.values());
    }

    /**
     * 根据session获取连接id
     */
    public String getIdBySession(Session session) {
        return sid2uid.get(session.getId());
    }

    /**
     * 根据session获取连接id
     */
    public String getIdBySessionId(String sessionId) {
        return sid2uid.get(sessionId);
    }

}
