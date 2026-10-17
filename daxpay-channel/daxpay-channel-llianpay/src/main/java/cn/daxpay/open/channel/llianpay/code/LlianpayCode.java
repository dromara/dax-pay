package cn.daxpay.open.channel.llianpay.code;

/// # 连连支付通道常量(主应用侧)
///
/// 集中管理连连回调应答、交易状态等业务常量, 与子应用 LlianpayCode 对齐。
public final class LlianpayCode {

    private LlianpayCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给连连的兜底应答串(正常路径应答由子应用生成 Success)
    public static final String NOTIFY_FAIL = "Fail";

    // ===== 连连交易状态(与子应用对齐) =====

    /// 交易成功(txn_status / refund_status = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 交易失败(FAILURE)
    public static final String TRADE_STATUS_FAIL = "FAILURE";

    /// 交易已退回(CANCEL, 视同失败)
    public static final String TRADE_STATUS_CANCEL = "CANCEL";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "LLIANPAY";
}
