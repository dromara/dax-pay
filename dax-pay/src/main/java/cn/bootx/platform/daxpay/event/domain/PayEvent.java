package cn.bootx.platform.daxpay.event.domain;

import cn.bootx.platform.common.jackson.util.JacksonUtil;

/**
 * Mq事件消息定义
 * @author xxm
 * @since 2023/7/17
 */
public interface PayEvent {
    /** MQ队列名称 */
    String getQueueName();
    /** 要发送的消息体 */
    default Object toMessage(){
        return JacksonUtil.toJson(this);
    }

}
