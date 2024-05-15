package cn.bootx.platform.common.websocket.notice;

import cn.bootx.platform.common.websocket.func.WsUserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.common.core.code.CommonCode.USER_ID;
import static cn.bootx.platform.common.core.code.WebHeaderCode.ACCESS_TOKEN;

/**
 * 全局用户WS通知拦截鉴权
 *
 * @author xxm
 * @since 2022/6/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserNoticeWebSocketInterceptor implements HandshakeInterceptor {

    private final WsUserAuthService wsUserAuthService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter(ACCESS_TOKEN);
        Long userId = wsUserAuthService.getUserIdByToken(token);
        if (Objects.isNull(userId)) {
            return false;
        }
        attributes.put(USER_ID, userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {

    }

}
