package cn.daxpay.open.channel.shouqianba.code;

/// # 收钱吧支付通道常量(主应用侧)
///
/// 集中管理收钱吧回调应答、订单状态等业务常量, 与子应用 ShouqianbaCode 对齐。
/// 订单状态(order_status): PAID 已支付 / FAIL 失败 / CANCELED 已取消 / REFUNDED·PARTIAL_REFUNDED 已退款。
public final class ShouqianbaCode {

    private ShouqianbaCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给收钱吧的纯文本应答(对齐收钱吧协议: 非该串会持续重发)
    public static final String NOTIFY_SUCCESS = "success";

    /// 通知处理失败时返回给收钱吧的纯文本应答
    public static final String NOTIFY_FAIL = "fail";

    // ===== 收钱吧订单状态(与子应用对齐) =====

    /// 已支付
    public static final String ORDER_STATUS_PAID = "PAID";

    /// 支付失败
    public static final String ORDER_STATUS_FAIL = "FAIL";

    /// 已取消
    public static final String ORDER_STATUS_CANCELED = "CANCELED";

    /// 已全额退款(支付曾成功)
    public static final String ORDER_STATUS_REFUNDED = "REFUNDED";

    /// 已部分退款(支付曾成功)
    public static final String ORDER_STATUS_PARTIAL_REFUNDED = "PARTIAL_REFUNDED";

    // ===== 回调通知状态 =====

    /// 回调通知状态成功(status 字段)
    public static final String NOTIFY_STATUS_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "SHOUQIANBA";
}
