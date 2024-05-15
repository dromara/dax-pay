package cn.bootx.platform.starter.wechat.configuration;

import cn.bootx.platform.starter.wechat.handler.WeChatMpMessageHandler;
import cn.bootx.platform.starter.wechat.handler.WeChatMsgHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 微信信息路由配置
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeChatMessageRouterConfiguration {

    private final List<WeChatMpMessageHandler> weChatMpMessageHandlers;

    private final WeChatMsgHandler weChatMsgHandler;

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 记录日志

        // 消息路由绑定
        for (WeChatMpMessageHandler weChatMpMessageHandler : weChatMpMessageHandlers) {
            router.rule()
                .async(false)
                .msgType(weChatMpMessageHandler.getMsgType())
                .event(weChatMpMessageHandler.getEvent())
                .handler(weChatMpMessageHandler)
                .end();
        }
        // 默认的 文本消息处理
        router.rule().async(false).handler(weChatMsgHandler).end();
        return router;
    }

}
