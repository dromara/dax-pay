package cn.daxpay.open.channel.yeepay.code;

/// # 易宝支付通道常量(主应用侧)
///
/// 集中管理易宝回调应答、交易状态等业务常量。
public final class YeepayCode {

    private YeepayCode() {
    }

    // ===== 通知应答(返回给易宝) =====

    /// 通知处理成功
    public static final String NOTIFY_SUCCESS = "SUCCESS";

    /// 通知处理失败
    public static final String NOTIFY_FAIL = "FAIL";

    // ===== 统一交易状态(与子应用对齐) =====

    /// 成功
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 失败
    public static final String TRADE_STATUS_FAIL = "FAIL";

    /// 已关闭
    public static final String TRADE_STATUS_CLOSED = "CLOSED";

    /// 处理中
    public static final String TRADE_STATUS_PROGRESS = "PROGRESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "YEEPAY";
}
