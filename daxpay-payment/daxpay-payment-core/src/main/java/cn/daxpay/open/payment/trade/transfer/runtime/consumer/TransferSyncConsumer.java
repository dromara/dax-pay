package cn.daxpay.open.payment.trade.transfer.runtime.consumer;

import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.trade.transfer.runtime.mq.TransferSyncMessage;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferSyncService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 转账延迟同步消费者
///
/// 监听 [PayArtemisConstants#TRANSFER_SYNC_QUEUE]，消费转账返回"处理中"后
/// 延时 2 分钟投递的同步消息，触发通道状态查询。
///
/// 幂等保障：[TransferSyncService#autoSync] 内部基于凭证状态前置校验 +
/// Redis 分布式锁保证同一凭证只同步一次，消费端无需额外去重。
///
/// 异常处理：消费失败只记 error 日志，不向上抛出，避免触发 JMS 默认重试风暴。
/// 漏单由定时同步任务兜底扫描补救。
@Slf4j
@Component
@RequiredArgsConstructor
public class TransferSyncConsumer {

    private final TransferSyncService transferSyncService;

    /// 消费转账延迟同步消息
    ///
    /// @param json 消息体 JSON，见 [TransferSyncMessage]
    @JmsListener(destination = PayArtemisConstants.TRANSFER_SYNC_QUEUE)
    public void onMessage(String json) {
        TransferSyncMessage message;
        try {
            // 统一 Text 传输，消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, TransferSyncMessage.class);
        } catch (Exception e) {
            // 消息体解析失败属于毒消息，直接丢弃避免无限重试
            log.warn("转账同步消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        try {
            transferSyncService.autoSync(message.getTransferNo());
        } catch (Exception e) {
            // 不向上抛出，避免 JMS 重试风暴；漏单由定时任务兜底
            log.error("转账同步处理失败, transferNo={}", message.getTransferNo(), e);
        }
    }
}
