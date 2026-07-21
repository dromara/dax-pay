package cn.daxpay.open.payment.trade.runtime.mq;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知发送消息体
///
/// 消费端只信任 taskId 重新查库，不信任消息内业务状态
@Data
@Accessors(chain = true)
public class MchNoticeSendMessage {

    /// 通知任务 ID
    private Long taskId;
}
