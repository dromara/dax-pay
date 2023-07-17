package cn.bootx.platform.daxpay.mq.vender.rocket.listener;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * rocketMQ 消息监听
 * @author xxm
 * @since 2023/7/17
 */
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rocket")
public class PayRocketMqMsgListener {
}
