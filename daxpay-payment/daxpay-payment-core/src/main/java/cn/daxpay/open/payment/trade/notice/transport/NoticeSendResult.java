package cn.daxpay.open.payment.trade.notice.transport;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知单次发送结果
///
/// 由 [NoticeTransportSender] 返回, 引擎据此落流水与排期重试
@Data
@Accessors(chain = true)
public class NoticeSendResult {

    /// 是否业务 Ack 成功 (HTTP: 2xx + body=SUCCESS; MQ: publish 成功)
    private boolean success;

    /// HTTP 状态码（MQ 投递时可空）
    private Integer httpStatus;

    /// 错误或非 SUCCESS 响应摘要
    private String errorMsg;

    /// 请求摘要（便于排查）
    private String requestDigest;
}
