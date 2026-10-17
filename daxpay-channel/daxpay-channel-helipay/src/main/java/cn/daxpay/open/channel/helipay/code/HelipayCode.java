package cn.daxpay.open.channel.helipay.code;

/// # 合利宝支付通道常量(主应用侧)
///
/// 集中管理合利宝回调应答、交易状态等业务常量, 与子应用
/// [cn.daxpay.open.channel.helipay.code.HelipayCode] 对齐。
public final class HelipayCode {

    private HelipayCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功应答(纯文本 success, 处理失败返回 fail 由通道重试)
    public static final String NOTIFY_SUCCESS = "success";

    /// 通知处理失败应答(纯文本 fail)
    public static final String NOTIFY_FAIL = "fail";

    // ===== 合利宝交易状态(与子应用对齐) =====

    /// 订单状态: 成功(rt7_orderStatus / 回调 rt4_status = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "HELIPAY";
}
