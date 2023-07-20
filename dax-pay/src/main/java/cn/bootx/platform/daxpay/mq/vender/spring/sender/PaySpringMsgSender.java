package cn.bootx.platform.daxpay.mq.vender.spring.sender;

import cn.bootx.platform.daxpay.mq.PayMqMsgSender;
import cn.bootx.platform.daxpay.mq.event.PayEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Spring 事件方式
 * @author xxm
 * @since 2023/7/17
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "spring", matchIfMissing = true)
public class PaySpringMsgSender implements PayMqMsgSender {
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 实时推送MQ消息
     *
     * @param msg 消息
     */
    @Override
    public void send(PayEvent msg) {
        applicationEventPublisher.publishEvent(msg);
    }

    /**
     * 推送MQ延迟消息
     *
     * @param msg   消息
     * @param delay 延迟时间, 单位秒
     */
    @Override
    public void send(PayEvent msg, int delay) {
        applicationEventPublisher.publishEvent(msg);
    }
}
