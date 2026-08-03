package cn.daxpay.open.payment.trade.notice.command;

import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知派发命令
///
/// 业务域只构造本对象，不把订单实体传入发送层
@Data
@Accessors(chain = true)
public class NoticeDispatchCommand {

    /// 商户号
    private String mchNo;

    /// 应用号
    private String appId;

    /// 通知事件码（如 pay.success）
    private String event;

    /// 业务主键
    private Long bizId;

    /// 业务单号
    private String bizNo;

    /// 订单级 notifyUrl（可空）
    private String orderNotifyUrl;

    /// 传输通道，默认 HTTP
    private NoticeTransportEnum transport = NoticeTransportEnum.HTTP;

    /// 报文格式，默认 SYSTEM
    private NoticeFormatEnum format = NoticeFormatEnum.SYSTEM;

    /// 内容策略
    private NoticeContentModeEnum contentMode = NoticeContentModeEnum.SNAPSHOT;

    /// 快照 JSON 或引用指针 JSON
    private String contentOrRef;

    /// 协议适配层自带 URL（format 非 SYSTEM 时使用，如易支付）
    private String protocolNotifyUrl;
}
