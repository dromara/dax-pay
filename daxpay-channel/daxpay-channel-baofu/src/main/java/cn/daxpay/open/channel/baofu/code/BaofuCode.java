package cn.daxpay.open.channel.baofu.code;

/// # 宝付支付通道常量(主应用侧)
///
/// 集中管理宝付回调应答、交易状态等业务常量。
/// 宝付交易状态 txnState = SUCCESS 成功(与子应用 [BaofuCode] 对齐)。
public final class BaofuCode {

    private BaofuCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理成功时返回给宝付的应答串(纯文本, 对齐参考实现)
    public static final String NOTIFY_SUCCESS = "OK";

    /// 通知处理失败时返回给宝付的应答串(验签失败/业务异常)
    public static final String NOTIFY_FAIL = "FAIL";

    /// 通知报文缺失时返回给宝付的应答串
    public static final String NOTIFY_NO = "NO";

    // ===== 宝付交易状态(与子应用对齐) =====

    /// 交易成功(txnState = SUCCESS)
    public static final String TRADE_STATUS_SUCCESS = "SUCCESS";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "BAOFU";
}
