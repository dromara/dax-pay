package cn.daxpay.open.payment.trade.notice.transport;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # MQ 传输发送器
///
/// 将 [NoticeEnvelope].body 发布到 task.url(Artemis Topic, 如 daxpay.notice.<appId>),
/// publish 成功即视为投递成功(ACK 语义对齐 Stripe EventBridge: 推到事件总线即完成, 消费侧失败由商户/MQ 自身负责)。
/// 商户侧用 JMS 持久订阅消费, 离线不丢消息
@Slf4j
@Component
@RequiredArgsConstructor
public class MqTransportSender implements NoticeTransportSender {

    private final ArtemisTemplateService artemisTemplateService;

    @Override
    public String transport() {
        return NoticeTransportEnum.MQ.getCode();
    }

    @Override
    public NoticeSendResult send(MchNoticeTask task, NoticeEnvelope envelope) {
        NoticeSendResult result = new NoticeSendResult();
        result.setRequestDigest(envelope.getRequestDigest());
        try {
            // 投递到商户通知 Topic (task.url 在 MQ 方式下存 Topic 名)
            artemisTemplateService.sendTopic(task.getUrl(), envelope.getBody());
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("MQ 通知投递失败, taskId={}, bizNo={}, topic={}",
                    task.getId(), task.getBizNo(), task.getUrl(), e);
            result.setSuccess(false).setErrorMsg(e.getMessage());
        }
        return result;
    }
}
