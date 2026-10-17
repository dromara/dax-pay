package cn.daxpay.open.channel.sand.code;

/// # 杉德支付通道常量(主应用侧)
///
/// 集中管理杉德回调应答、订单状态等业务常量。
/// 杉德订单状态为字符串码: success 成功 / fail 失败 / 其他处理中, 与子应用 [SandCode] 对齐。
public final class SandCode {

    private SandCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给杉德的应答串(纯文本, 杉德规约)
    public static final String NOTIFY_SUCCESS = "respCode=000000";

    /// 通知处理失败时返回给杉德的应答串(纯文本, 杉德规约)
    public static final String NOTIFY_FAIL = "respCode=020002";

    // ===== 杉德订单状态(与子应用对齐) =====

    /// 交易成功(orderStatus = success)
    public static final String ORDER_STATUS_SUCCESS = "success";

    /// 交易失败(orderStatus = fail)
    public static final String ORDER_STATUS_FAIL = "fail";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "SAND";
}
