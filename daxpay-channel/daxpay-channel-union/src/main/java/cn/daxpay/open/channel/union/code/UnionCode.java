package cn.daxpay.open.channel.union.code;

/// # 云闪付支付常量
///
/// 银联 ACP 回调为 form 参数, 验签后需返回 "ok" 应答。
/// 统一状态码 SUCCESS/PROGRESS/CLOSED 由子应用映射。
public interface UnionCode {

    /// 银联回调成功应答(银联要求返回 ok)
    String NOTIFY_SUCCESS = "ok";

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

    /// 银联 ACP 签名类型
    String SIGN_TYPE_RSA2 = "RSA2";

    /// 货币种类
    String CURRENCY_CNY = "CNY";
}
