package cn.bootx.platform.daxpay.mq;

import cn.bootx.platform.daxpay.mq.event.PayEvent;

/**
 * 支付消息队列发送接口定义
 * @author xxm
 * @since 2023/7/17
 */
public interface PayMqMsgSender {

    /**
     * 实时推送MQ消息
     * @param msg 消息
     */
    void send(PayEvent msg);

    /**
     * 推送MQ延迟消息
     * @param msg 消息
     * @param delay 延迟时间, 单位秒
     */
    void send(PayEvent msg, int delay);
}
