package cn.daxpay.open.channel.jlpay.code;

/// # 嘉联支付通道常量(主应用侧)
///
/// 集中管理嘉联回调应答、交易状态等业务常量。
/// 嘉联支付状态: 2 成功; tranSts 1 处理中(非 1 失败终态), 与子应用 [JlpayCode] 对齐。
public final class JlpayCode {

    private JlpayCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给嘉联的应答串(验签与业务均成功)
    public static final String NOTIFY_SUCCESS = "{\"ret_code\":\"00000\"}";

    /// 通知验签失败时返回给嘉联的应答串
    public static final String NOTIFY_SIGN_FAIL = "{\"ret_code\":\"00001\",\"ret_msg\":\"sign fail\"}";

    /// 通知报文缺失/解析失败时返回给嘉联的应答串
    public static final String NOTIFY_NO_DATA = "{\"ret_code\":\"00002\",\"ret_msg\":\"no data\"}";

    /// 通知业务处理异常时返回给嘉联的应答串(失败码触发嘉联重发通知, 由重试自愈)
    public static final String NOTIFY_PROCESS_FAIL = "{\"ret_code\":\"00001\",\"ret_msg\":\"process fail\"}";

    // ===== 嘉联交易状态(与子应用对齐) =====

    /// 支付状态: 成功(status)
    public static final String TRADE_STATUS_SUCCESS = "2";

    /// 交易处理状态: 处理中(tranSts, 非 1 为失败终态)
    public static final String TRAN_STS_PROCESSING = "1";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "JLPAY";
}
