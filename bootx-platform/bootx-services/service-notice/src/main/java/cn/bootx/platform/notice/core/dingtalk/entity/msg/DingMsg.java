package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;

/**
 * 钉钉消息接口
 *
 * @author xxm
 * @since 2022/7/20
 */
public interface DingMsg {

    /**
     * 转换成钉钉消息
     */
    Msg toDingMsg();

}
