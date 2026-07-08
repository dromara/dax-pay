package cn.daxpay.open.channel.leshua.code;

/// # 乐刷通道常量(主应用侧)
///
/// 仅保留主应用侧解析响应/回调时需要的状态码常量。
/// 网关地址、service 值等子应用专属常量见 channel-two 的 `LeshuaCode`。
public final class LeshuaCode {

    private LeshuaCode() {
    }

    // ===== 交易状态(返回值 status 字段) =====

    /// 支付中
    public static final String PAY_STATUS_PROGRESS = "0";

    /// 支付成功
    public static final String PAY_STATUS_SUCCESS = "2";

    /// 订单关闭
    public static final String PAY_STATUS_CLOSE = "6";

    /// 支付失败
    public static final String PAY_STATUS_FAIL = "8";

    // ===== 退款状态(返回值 status 字段) =====

    /// 退款中
    public static final String REFUND_STATUS_PROGRESS = "10";

    /// 退款成功
    public static final String REFUND_STATUS_SUCCESS = "11";

    /// 退款失败
    public static final String REFUND_STATUS_FAIL = "12";
}
