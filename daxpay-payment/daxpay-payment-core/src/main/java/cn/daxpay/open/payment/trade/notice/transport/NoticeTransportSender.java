package cn.daxpay.open.payment.trade.notice.transport;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;

/// # 商户出站通知传输发送器
///
/// 按 [cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum] 路由,
/// 负责把已组装好的 [NoticeEnvelope] 投递出去 (HTTP 回调 / MQ 推送), 与报文格式正交
public interface NoticeTransportSender {

    /// 传输通道编码（与 NoticeTransportEnum.code 对齐: http / mq）
    String transport();

    /// 执行一次投递
    NoticeSendResult send(MchNoticeTask task, NoticeEnvelope envelope);
}
