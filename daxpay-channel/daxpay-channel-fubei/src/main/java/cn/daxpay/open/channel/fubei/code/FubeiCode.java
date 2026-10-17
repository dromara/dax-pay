package cn.daxpay.open.channel.fubei.code;

/// # 付呗支付通道常量(主应用侧)
///
/// 集中管理付呗回调应答、订单状态等业务常量, 与子应用 [cn.daxpay.open.channel.fubei.code.FubeiCode] 对齐。
public final class FubeiCode {

    private FubeiCode() {
    }

    // ===== 通知应答(纯文本约定, 对齐参考插件) =====

    /// 通知处理成功时返回给付呗的应答
    public static final String NOTIFY_SUCCESS = "success";

    /// 通知处理失败(验签失败/业务异常)时返回给付呗的应答
    public static final String NOTIFY_FAIL = "fail";

    // ===== 付呗订单状态(与子应用对齐) =====

    /// 订单支付成功(order_status = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "FUBEI";
}
