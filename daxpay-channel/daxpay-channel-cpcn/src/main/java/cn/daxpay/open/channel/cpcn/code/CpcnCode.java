package cn.daxpay.open.channel.cpcn.code;

/// # 中金支付通道常量(主应用侧)
///
/// 集中管理中金回调应答、交易状态等业务常量。
/// 中金交易状态为两位码: 10 处理中 / 20 退款成功 / 30 交易成功 / 40 失败, 与子应用 [CpcnCode] 对齐。
public final class CpcnCode {

    private CpcnCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给中金的兜底应答(Base64 XML, Head.Code=2001; 验签通过的正常应答由子应用生成)
    public static final String NOTIFY_FAIL = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48UmVzcG9uc2UgdmVyc2lvbj0iMi4wIj48SGVhZD48Q29kZT4yMDAxPC9Db2RlPjxNZXNzYWdlPkZBSUw8L01lc3NhZ2U+PC9IZWFkPjwvUmVzcG9uc2U+";

    // ===== 中金交易状态(与子应用对齐) =====

    /// 处理中/未终态
    public static final String TRADE_STATUS_PROCESSING = "10";

    /// 退款成功(退款通知终态)
    public static final String TRADE_STATUS_REFUND_SUCCESS = "20";

    /// 交易成功(支付终态)
    public static final String TRADE_STATUS_SUCCESS = "30";

    /// 交易失败
    public static final String TRADE_STATUS_FAIL = "40";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "CPCN";
}
