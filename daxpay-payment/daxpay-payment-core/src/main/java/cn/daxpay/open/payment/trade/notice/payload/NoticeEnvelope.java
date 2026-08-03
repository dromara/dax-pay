package cn.daxpay.open.payment.trade.notice.payload;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知投递信封
///
/// 由 [NoticePayloadBuilder] 按 format 组装, 描述一次投递的请求形态。
/// 传输通道 [cn.daxpay.open.payment.trade.notice.transport.NoticeTransportSender] 据此投递:
/// - HTTP: 按 method 发请求 (POST 用 body, GET 用 url 含 query)
/// - MQ:   忽略 method, 将 body 发布到 task.url(Topic)
@Data
@Accessors(chain = true)
public class NoticeEnvelope {

    /// HTTP 方法 (POST / GET), 仅 HTTP 传输使用; MQ 传输忽略
    private String method;

    /// HTTP 完整请求 URL (GET 时含 query); MQ 时通常为 null
    private String url;

    /// 请求体 (POST JSON 或 MQ 推送的消息体); GET 时为 null
    private String body;

    /// 请求摘要(截断), 便于排查
    private String requestDigest;
}
