package cn.daxpay.open.channel.youzan.code;

/// # 有赞支付通道常量(主应用侧)
///
/// 集中管理有赞回调应答、交易状态等业务常量。
/// 有赞交易状态为字符串码: SUCCESS 成功(其余按处理中/失败由回调服务映射), 与子应用 [cn.daxpay.open.channel.youzan.code.YouzanCode] 对齐。
public final class YouzanCode {

    private YouzanCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给有赞的应答串(JSON 规约)
    public static final String NOTIFY_SUCCESS = "{\"success\":true}";

    /// 通知处理失败时返回给有赞的应答串(JSON 规约)
    public static final String NOTIFY_FAIL = "{\"success\":false,\"message\":\"FAIL\"}";

    // ===== 有赞交易状态(与子应用对齐) =====

    /// 交易成功(status = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "YOUZAN";
}
