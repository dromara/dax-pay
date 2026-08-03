package cn.daxpay.open.payment.trade.notice.payload;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;

/// # 商户出站通知报文构建器
///
/// 按 [cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum] 路由,
/// 仅负责组装报文内容 ([NoticeEnvelope]), 与传输通道正交
public interface NoticePayloadBuilder {

    /// 报文格式编码（与 NoticeFormatEnum.code 对齐: system / easy_pay）
    String format();

    /// 组装投递信封
    NoticeEnvelope build(MchNoticeTask task);
}
