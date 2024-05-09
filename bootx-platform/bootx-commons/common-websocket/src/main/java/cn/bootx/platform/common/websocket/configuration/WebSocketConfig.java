package cn.bootx.platform.common.websocket.configuration;

import cn.bootx.platform.common.websocket.notice.UserNoticeWebSocketHandler;
import cn.bootx.platform.common.websocket.notice.UserNoticeWebSocketInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 用户Websocket配置
 *
 * @author xxm
 * @since 2022/3/27
 */
@EnableWebSocket
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final UserNoticeWebSocketInterceptor userNoticeWebSocketInterceptor;

    private final UserNoticeWebSocketHandler userNoticeWebSocketHandler;

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     * plumelog 也导入了这个Bean, 在使用plumelog的情况下, 不在创建这个bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass("com.plumelog.lite.client.InitClientBean")
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 拦截器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            // WebSocket 连接处理器
            .addHandler(userNoticeWebSocketHandler, "/ws/user")
            // WebSocket 拦截器
            .addInterceptors(userNoticeWebSocketInterceptor)
            // 允许跨域
            .setAllowedOrigins("*");
    }

}
