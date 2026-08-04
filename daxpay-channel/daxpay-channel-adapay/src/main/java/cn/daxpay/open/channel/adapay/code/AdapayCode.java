package cn.daxpay.open.channel.adapay.code;

/// # Adapay 通道常量(主应用侧)
///
/// 定义回调应答与统一状态码。
/// 验签/加签全部转发子应用 dax-pay-channel-two 处理(主应用零加密代码),
/// 平台公钥 SHA1withRSA 验签所需公钥由子应用 [cn.daxpay.open.channel.adapay.code.AdapayCode#PLATFORM_PUBLIC_KEY] 内置兜底。
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
}
