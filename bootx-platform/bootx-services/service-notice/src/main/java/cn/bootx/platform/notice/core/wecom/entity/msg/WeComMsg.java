package cn.bootx.platform.notice.core.wecom.entity.msg;

import me.chanjar.weixin.cp.bean.message.WxCpMessage;

/**
 * 企业微信通知消息
 *
 * @author xxm
 * @since 2022/7/23
 */
public interface WeComMsg {

    /**
     * 转换成企业微信格式的消息
     * @return
     */
    WxCpMessage toMsg();

}
