package cn.daxpay.open.payment.trade.notice.protocol;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知协议发送器
///
/// 按 protocol 精确路由；禁止 fan-out 遍历全部插件
public interface NoticeProtocolSender {

    /// 协议编码（与 NoticeProtocolEnum.code 对齐）
    String protocol();

    /// 执行一次 HTTP/协议发送
    NoticeSendResult send(MchNoticeTask task);

    /// 单次发送结果
    @Data
    @Accessors(chain = true)
    class NoticeSendResult {
        /// 是否业务 Ack 成功
        private boolean success;
        /// HTTP 状态码（可空）
        private Integer httpStatus;
        /// 错误或非 SUCCESS 响应摘要
        private String errorMsg;
        /// 请求摘要（便于排查）
        private String requestDigest;
    }
}
