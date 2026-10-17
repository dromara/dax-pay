package cn.daxpay.open.channel.yizhifubj.code;

/// # 一直付(北京)支付通道常量(主应用侧)
///
/// 集中管理一直付回调应答、交易状态等业务常量, 与子应用 [YizhifubjCode] 对齐。
public final class YizhifubjCode {

    private YizhifubjCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功应答串(明文, 直接返回给一直付)
    public static final String NOTIFY_ACK_SUCCESS = "SUCCESS";

    /// 通知处理失败应答串(明文, 直接返回给一直付)
    public static final String NOTIFY_ACK_FAIL = "Fail";

    // ===== 一直付交易状态(与子应用对齐) =====

    /// 交易成功(status = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 交易失败(FAILED / CANCEL 视同失败)
    public static final String TRADE_STATUS_FAIL = "FAILED";

    /// 交易取消(视同失败)
    public static final String TRADE_STATUS_CANCEL = "CANCEL";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "YIZHIFUBJ";
}
