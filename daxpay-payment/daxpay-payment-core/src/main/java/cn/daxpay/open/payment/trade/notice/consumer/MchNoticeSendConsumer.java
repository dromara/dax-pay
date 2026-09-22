package cn.daxpay.open.payment.trade.notice.consumer;

import cn.daxpay.open.payment.trade.notice.service.NoticeSendEngine;
import cn.daxpay.open.payment.trade.runtime.mq.MchNoticeSendMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import java.util.Objects;

/// # 商户出站通知发送消费者
///
/// 失败不向上抛（避免 JMS 重试风暴）；业务重试由引擎延时队列负责
@Slf4j
@Component
@RequiredArgsConstructor
public class MchNoticeSendConsumer {

    private final NoticeSendEngine noticeSendEngine;

    // concurrency=8: HTTP 发送为同步阻塞(全局 RestClient 响应超时 40s), 默认单消费者在批量退款等
    // 高峰场景会积压; 8 并发下慢端点最坏也有约 72 笔/分钟吞吐, 正常亚秒级端点可达数百笔/分钟,
    // 且低于连接池每主机上限(maxPerRoute=20), 对单一商户回调地址冲击温和。
    // 多线程安全性: [NoticeSendEngine#sendAuto] 经 PaymentContext(ThreadLocal) 按线程独立装载租户身份
    @JmsListener(destination = PayArtemisConstants.MCH_NOTICE_SEND, concurrency = "8")
    public void onMessage(String json) {
        MchNoticeSendMessage message;
        try {
            message = JacksonUtil.toBean(json, MchNoticeSendMessage.class);
        } catch (Exception e) {
            log.warn("出站通知消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        if (Objects.isNull(message.getTaskId())) {
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
