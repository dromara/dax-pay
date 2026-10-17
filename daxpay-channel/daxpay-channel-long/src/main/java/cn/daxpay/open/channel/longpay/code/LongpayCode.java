package cn.daxpay.open.channel.longpay.code;

/// # 建行龙支付通道常量(主应用侧)
///
/// 集中管理建行龙支付回调应答、通知状态等业务常量。
/// 回调 SUCCESS 字段为 Y/N 单字符码, 与子应用 [LongpayCode] 对齐。
public final class LongpayCode {

    private LongpayCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给建行的兜底应答(正常应答 SUCCESS 由子应用生成, 仅验签失败时用本兜底串)
    public static final String NOTIFY_FAIL = "FAIL";

    // ===== 建行通知状态(与子应用对齐) =====

    /// 支付成功(SUCCESS=Y)
    public static final String TRADE_STATUS_SUCCESS = "Y";

    /// 支付失败(SUCCESS=N)
    public static final String TRADE_STATUS_FAIL = "N";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "LONGPAY";
}
