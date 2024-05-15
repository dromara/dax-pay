package cn.bootx.platform.starter.wechat.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;

/**
 * 处理微信推送消息的处理器接口 (进行微封装)
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface WeChatMpMessageHandler extends WxMpMessageHandler {

    /**
     * 消息类型
     * @see WxConsts.XmlMsgType
     */
    default String getMsgType() {
        return WxConsts.XmlMsgType.EVENT;
    }

    /**
     * event值
     * @see WxMpEventConstants
     * @see WxConsts.XmlMsgType
     */
    String getEvent();

}
