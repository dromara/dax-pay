package cn.bootx.platform.daxpay.event;

import cn.bootx.platform.daxpay.event.domain.PayCancelEvent;
import cn.bootx.platform.daxpay.event.domain.PayCompleteEvent;
import cn.bootx.platform.daxpay.event.domain.PayExpiredTimeEvent;
import cn.bootx.platform.daxpay.event.domain.PayRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 支付中心消息发送器
 *
 * @author xxm
 * @since 2021/4/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayEventSender {

    private final PayMqMsgSender mqMsgSender;

    /**
     * 支付完成 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayComplete(PayCompleteEvent event) {
        mqMsgSender.send(event);
    }

    /**
     * 支付撤销/关闭 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayCancel(PayCancelEvent event) {
        mqMsgSender.send(event);
    }

    /**
     * 支付退款 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayRefund(PayRefundEvent event) {
        mqMsgSender.send(event);
    }

    /**
     * 支付单超时 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayExpiredTime(PayExpiredTimeEvent event) {
        mqMsgSender.send(event);
    }

}
