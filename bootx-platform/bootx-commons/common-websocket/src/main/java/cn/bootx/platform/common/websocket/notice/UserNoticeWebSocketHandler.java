package cn.bootx.platform.common.websocket.notice;

import cn.bootx.platform.common.websocket.manager.SpringWebSocketSessionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.bootx.platform.common.core.code.CommonCode.USER_ID;

/**
 * 全局用户WS通知
 *
 * @author xxm
 * @since 2022/3/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserNoticeWebSocketHandler extends TextWebSocketHandler {

    /** websocket连接管理器 */
    @Getter
    private static final SpringWebSocketSessionManager wsManager = new SpringWebSocketSessionManager();

    /** 记录当前在线连接数 */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 连接建立成功调用的方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get(USER_ID);
        wsManager.addSession(String.valueOf(userId), session);
        onlineCount.incrementAndGet(); // 在线数加1
        log.info("有新连接加入：{}，当前在线人数为：{}", userId, onlineCount.get());
    }

    /**
     * 关闭
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        onlineCount.decrementAndGet(); // 在线数减1
        String userId = wsManager.getIdBySession(session);
        wsManager.removeSession(session);
        log.info("有一连接关闭：{}，当前在线人数为：{}", userId, onlineCount.get());
    }

    /**
     * 接收消息
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 不处理接收的消息, 通常只会接收到心跳请求
//         log.debug("心跳请求");
    }

    /**
     * 错误
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("{} 发生错误", session.getId());
    }

    /**
     * 服务端发送消息给客户端(指定用户)
     */
    public void sendMessageByUser(String message, Long userId) {
        try {
            List<WebSocketSession> sessions = wsManager.getSessionsByUserId(String.valueOf(userId));

            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(message));
            }
        }
        catch (Exception e) {
            log.error("服务端发送消息给客户端失败：", e);
        }
    }

    /**
     * 服务端发送消息给客户端(指定用户组)
     */
    public void sendMessageByUsers(String message, List<Long> userIds) {
        for (Long userId : userIds) {
            this.sendMessageByUser(message, userId);
        }
    }

    /**
     * 服务端发送消息给客户端(全体发送)
     */
    public void sendMessageByAll(String message) {
        try {
            List<WebSocketSession> sessions = wsManager.getSessions();
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(message));
            }
        }
        catch (Exception e) {
            log.error("服务端发送消息给客户端失败：", e);
        }
    }

}
