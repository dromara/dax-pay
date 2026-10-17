package cn.daxpay.open.channel.umfpay.code;

/// # 联动优势支付通道常量(主应用侧)
///
/// 集中管理联动优势回调应答、交易状态等业务常量, 与子应用 [UmfpayCode] 对齐。
public final class UmfpayCode {

    private UmfpayCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给联动优势的兜底应答(正常应答为子应用生成的 HTML META 页, 仅验签失败时用本兜底串)
    public static final String NOTIFY_FAIL = "ERROR";

    // ===== 联动优势交易状态(与子应用对齐) =====

    /// 交易成功(回调 trade_state)
    public static final String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "UMFPAY";
}
