package cn.daxpay.open.payment.trade.transfer.runtime.mq;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账延迟同步消息
///
/// 转账返回"处理中"后延时投递到 [cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants#TRANSFER_SYNC_QUEUE]，
/// 由 TransferSyncConsumer 消费触发通道状态查询。
/// 消息体只携带平台转账单号，幂等与防重由同步服务的分布式锁保证。
@Data
@Accessors(chain = true)
public class TransferSyncMessage {

    /// 平台转账单号
    private String transferNo;
}
