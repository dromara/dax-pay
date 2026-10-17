package cn.daxpay.open.channel.kuaiqian.code;

/// # 快钱支付通道常量(主应用侧)
///
/// 集中管理快钱回调应答、交易状态等业务常量。
/// 快钱交易状态为单字符码: S 成功 / F 失败 / R 拒绝(视同失败), 与子应用 [KuaiqianCode] 对齐。
public final class KuaiqianCode {

    private KuaiqianCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给快钱的兜底应答(A9005 规约应答由子应用加密加签生成, 仅解密验签失败时用本兜底串)
    public static final String NOTIFY_FAIL = "ERROR";

    // ===== 快钱交易状态(与子应用对齐) =====

    /// 交易成功(orderStatus / txnStatus = S)
    public static final String TRADE_STATUS_SUCCESS = "S";

    /// 交易失败
    public static final String TRADE_STATUS_FAIL = "F";

    /// 交易拒绝(视同失败)
    public static final String TRADE_STATUS_REJECT = "R";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "KUAIQIAN";
}
