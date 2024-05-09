package cn.bootx.platform.starter.wechat.handler.qrscene;

import cn.bootx.platform.starter.wechat.core.login.service.WeChatQrLoginService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static cn.bootx.platform.starter.wechat.code.WeChatCode.EVENT_KEY_QRSCENE;
import static cn.bootx.platform.starter.wechat.code.WeChatCode.QRSCENE_LOGIN;

/**
 * 微信扫码登录
 *
 * @author xxm
 * @since 2023/3/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatQrLoginHandler implements WeChatQrSceneMsgHandler {

    private final WeChatQrLoginService weChatQrLoginService;

    /**
     * 消息前缀
     */
    @Override
    public String getPrefix() {
        return EVENT_KEY_QRSCENE + QRSCENE_LOGIN;
    }

    /**
     * 处理消息
     */
    @Override
    public WxMpXmlOutMessage handler(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        // 将扫码获取的二维码key值进行保存
        String qrCodeKey = StrUtil.subAfter(wxMessage.getEventKey(), getPrefix(), false);
        weChatQrLoginService.setOpenId(qrCodeKey, openId);
        return new TextBuilder().fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .content("感谢关注")
            .build();
    }

}
