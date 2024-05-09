package cn.bootx.platform.starter.wechat.handler;

import cn.bootx.platform.starter.wechat.handler.qrscene.WeChatQrSceneMsgHandler;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 新增关注订阅消息
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatSubscribeHandler implements WeChatMpMessageHandler {

    private final List<WeChatQrSceneMsgHandler> weChatQrSceneMsgHandlers;

    @Override
    public String getEvent() {
        return WxConsts.EventType.SUBSCRIBE;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        // 处理关注事件
        log.info("新关注用户 OPENID: " + openId);

        // 判断是否携带扫码参数
        for (WeChatQrSceneMsgHandler msgHandler : weChatQrSceneMsgHandlers) {
            if (StrUtil.startWith(wxMessage.getEventKey(), msgHandler.getPrefix())) {
                WxMpXmlOutMessage message = msgHandler.handler(wxMessage, context, wxMpService, sessionManager);
                if (Objects.nonNull(message)) {
                    return message;
                }
            }
        }
        return new TextBuilder().fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .content("感谢关注")
            .build();
    }

}
