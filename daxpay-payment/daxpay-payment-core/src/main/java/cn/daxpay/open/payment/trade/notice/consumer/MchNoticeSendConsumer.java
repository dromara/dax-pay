package cn.daxpay.open.payment.trade.notice.consumer;

import cn.daxpay.open.payment.trade.notice.service.NoticeSendEngine;
import cn.daxpay.open.payment.trade.runtime.mq.MchNoticeSendMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 商户出站通知发送消费者
///
/// 失败不向上抛（避免 JMS 重试风暴）；业务重试由引擎延时队列负责
@Slf4j
@Component
@RequiredArgsConstructor
public class MchNoticeSendConsumer {

    private final NoticeSendEngine noticeSendEngine;

    @JmsListener(destination = PayArtemisConstants.MCH_NOTICE_SEND)
    public void onMessage(String json) {
        MchNoticeSendMessage message;
        try {
            message = JacksonUtil.toBean(json, MchNoticeSendMessage.class);
        } catch (Exception e) {
            log.warn("出站通知消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        if (message.getTaskId() == null) {
            log.warn("出站通知消息缺少 taskId, 丢弃: json={}", json);
            return;
        }
        try {
            noticeSendEngine.sendAuto(message.getTaskId());
        } catch (Exception e) {
            log.error("出站通知消费失败, taskId={}", message.getTaskId(), e);
        }
    }
}
