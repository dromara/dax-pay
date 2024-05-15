package cn.bootx.platform.starter.wechat.handler.qrscene;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * 微信扫码消息处理
 *
 * @author xxm
 * @since 2023/3/21
 */
public interface WeChatQrSceneMsgHandler {

    /**
     * 消息前缀
     */
    String getPrefix();

    /**
     * 处理消息
     */
    WxMpXmlOutMessage handler(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager);

}
