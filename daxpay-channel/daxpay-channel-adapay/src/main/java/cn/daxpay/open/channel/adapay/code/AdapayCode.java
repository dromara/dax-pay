package cn.daxpay.open.channel.adapay.code;

/// # Adapay 通道常量(主应用侧)
///
/// 定义回调应答、统一状态码与Adapay 平台公钥(回调验签用)。
/// Adapay 回调验签与请求响应一致: data + signature, 用平台公钥 SHA1withRSA 验签,
/// 因平台公钥全局唯一, 主应用直接验签(不需转发子应用, 不需 channelMchNo 定位凭证)。
public interface AdapayCode {

    /// 回调成功应答(Adapay 要求返回 success)
    String NOTIFY_SUCCESS = "success";

    /// 回调失败应答
    String NOTIFY_FAIL = "fail";

    /// 统一交易状态-支付成功
    String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 统一交易状态-进行中
    String TRADE_STATUS_PROGRESS = "PROGRESS";

    /// 统一交易状态-已关闭
    String TRADE_STATUS_CLOSED = "CLOSED";

    /// 退款状态-成功
    String REFUND_STATUS_SUCCESS = "SUCCESS";

    /// 退款状态-处理中
    String REFUND_STATUS_PROCESSING = "PROCESSING";

    /// 退款状态-失败
    String REFUND_STATUS_FAIL = "FAIL";

    /// Adapay 平台公钥(控制台可见, 用于回调响应验签, RSA X509 Base64 字符串)
    /// 与子应用 [cn.daxpay.open.channel.adapay.code.AdapayCode.PLATFORM_PUBLIC_KEY] 一致
    String PLATFORM_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwN6xgd6Ad8v2hIIsQVnbt8a3JituR8o4Tc3B5WlcFR55bz4OMqrG/356Ur3cPbc2Fe8ArNd/0gZbC9q56Eb16JTkVNA/fye4SXznWxdyBPR7+guuJZHc/VW2fKH2lfZ2P3Tt0QkKZZoawYOGSMdIvO+WqK44updyax0ikK6JlNQIDAQAB";
}
