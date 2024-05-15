package cn.bootx.platform.starter.wechat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 菜单点击事件
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatMenuHandler implements WeChatMpMessageHandler {

    @Override
    public String getEvent() {
        return WxConsts.MenuButtonType.CLICK;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) {
        return null;
    }

}
