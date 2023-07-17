package cn.bootx.platform.daxpay.mq.event;

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
        return this;
    };

}
