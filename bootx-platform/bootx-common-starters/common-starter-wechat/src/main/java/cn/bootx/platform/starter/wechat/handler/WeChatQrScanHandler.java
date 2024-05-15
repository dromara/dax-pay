package cn.bootx.platform.starter.wechat.handler;

import cn.bootx.platform.starter.wechat.handler.qrscene.WeChatQrSceneMsgHandler;
import cn.bootx.platform.starter.wechat.core.login.service.WeChatQrLoginService;
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
 * 微信扫码事件
 *
 * @author xxm
 * @since 2022/8/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatQrScanHandler implements WeChatMpMessageHandler {

    private final WeChatQrLoginService weChatQrLoginService;

    private final List<WeChatQrSceneMsgHandler> weChatQrSceneMsgHandlers;

    @Override
    public String getEvent() {
        return WxConsts.EventType.SCAN;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
            WxSessionManager wxSessionManager) {
        // 扫描用户的OpenId
        String openId = wxMessage.getFromUser();
        // 判断是否携带扫码参数
        for (WeChatQrSceneMsgHandler msgHandler : weChatQrSceneMsgHandlers) {
            if (StrUtil.startWith(wxMessage.getEventKey(), msgHandler.getPrefix())) {
                WxMpXmlOutMessage message = msgHandler.handler(wxMessage, map, wxMpService, wxSessionManager);
                if (Objects.nonNull(message)) {
                    return message;
                }
            }
        }
        // 二维码key值
        String qrCodeKey = wxMessage.getEventKey();
        weChatQrLoginService.setOpenId(qrCodeKey, openId);

        return new TextBuilder().fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .content("感谢关注")
            .build();
    }

}
