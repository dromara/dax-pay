package cn.daxpay.open.payment.trade.alloc.runtime.mq;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 分账延迟同步消息
///
/// 消息体只携带平台分账单号, 幂等与防重由同步服务的分布式锁 + 状态守卫保证。
@Data
@Accessors(chain = true)
public class AllocSyncMessage {

    /// 平台分账单号
    private String allocNo;
}
