package cn.daxpay.open.channel.xingyi.code;

/// # 星驿付支付通道常量(主应用侧)
///
/// 集中管理星驿付回调应答与通道商户号前缀等业务常量。
public final class XingyiCode {

    private XingyiCode() {
    }

    // ===== 通知应答 =====

    /// 通知处理失败时返回给星驿付的兜底应答(JSON, 正常应答串由子应用生成)
    public static final String NOTIFY_FAIL = "{\"rspCod\":\"000002\",\"rspMsg\":\"sign fail\"}";

    // ===== 通道商户号前缀 =====

    /// 通道商户号生成前缀
    public static final String CHANNEL_MCH_NO_PREFIX = "XINGYI";
}
