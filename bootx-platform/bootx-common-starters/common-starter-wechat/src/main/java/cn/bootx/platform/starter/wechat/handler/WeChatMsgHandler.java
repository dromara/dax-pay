package cn.bootx.platform.starter.wechat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信接受文本消息处理
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatMsgHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) {

        // 回复消息的时候发送人和接收人要反过来
        return WxMpXmlOutMessage.TEXT()
            .toUser(wxMessage.getFromUser())
            .fromUser(wxMessage.getToUser())
            .content("我复述了一遍: " + wxMessage.getContent())
            .build();
    }

}
