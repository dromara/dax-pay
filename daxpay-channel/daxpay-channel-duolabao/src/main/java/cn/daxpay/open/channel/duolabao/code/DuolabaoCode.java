package cn.daxpay.open.channel.duolabao.code;

/// # 哆啦宝支付通道常量(主应用侧)
///
/// 集中管理哆啦宝回调应答、订单状态与通道商户号前缀等业务常量,
/// 与子应用 [cn.daxpay.open.channel.duolabao.code.DuolabaoCode] 对齐。
public final class DuolabaoCode {

    private DuolabaoCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给哆啦宝的兜底应答(纯文本, 按参考插件约定)
    public static final String NOTIFY_FAIL = "error";

    // ===== 哆啦宝订单状态(与子应用对齐) =====

    /// 订单状态: 支付成功
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 订单状态: 支付失败
    public static final String TRADE_STATUS_FAIL = "FAIL";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "DUOLABAO";
}
