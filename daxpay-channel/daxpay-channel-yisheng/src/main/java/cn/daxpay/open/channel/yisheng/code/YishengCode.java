package cn.daxpay.open.channel.yisheng.code;

/// # 易生支付通道常量(主应用侧)
///
/// 集中管理易生回调应答、交易状态等业务常量。
/// 异步通知 trade_status = TRADE_FINISHED 成功; 查询 trade_status = SUCCESS 成功, 与子应用 [YishengCode] 对齐。
public final class YishengCode {

    private YishengCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给易生的应答串(纯文本, 对齐参考实现)
    public static final String NOTIFY_SUCCESS = "SUCCESS";

    /// 通知处理失败时返回给易生的应答串(验签失败/业务异常)
    public static final String NOTIFY_FAIL = "FAIL";

    // ===== 易生交易状态(与子应用对齐) =====

    /// 异步通知交易成功(trade_status = TRADE_FINISHED)
    public static final String NOTIFY_TRADE_SUCCESS = "TRADE_FINISHED";

    /// 查询交易成功(trade_status = SUCCESS)
    public static final String QUERY_TRADE_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "YISHENG";
}
