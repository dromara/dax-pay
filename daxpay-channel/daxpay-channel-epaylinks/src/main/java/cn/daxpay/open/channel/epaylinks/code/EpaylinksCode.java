package cn.daxpay.open.channel.epaylinks.code;

/// # 易票联支付通道常量(主应用侧)
///
/// 集中管理易票联回调应答、交易状态等业务常量。
/// 易票联交易状态为两位码: 00 成功 / 01 失败 / 其他处理中, 与子应用 [EpaylinksCode] 对齐。
public final class EpaylinksCode {

    private EpaylinksCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给易票联的兜底应答 JSON(规约应答由子应用生成, 仅验签失败时用本兜底串)
    public static final String NOTIFY_FAIL = "{\"returnCode\":\"0001\",\"returnMsg\":\"fail\"}";

    // ===== 易票联交易状态(与子应用对齐) =====

    /// 交易成功(payState = 00)
    public static final String TRADE_STATUS_SUCCESS = "00";

    /// 交易失败(payState = 01)
    public static final String TRADE_STATUS_FAIL = "01";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "EPAYLINKS";
}
