package cn.daxpay.open.channel.fuyou.client.enums;

/// # 富友支付内容类型(主应用侧)
public enum FuyouPayBodyType {

    /// 二维码链接
    QR_CODE,
    /// JSAPI 调起参数
    JSAPI,
    /// 标识符
    IDENTIFIER,
    /// 跳转链接(H5 聚合收银台 token_url / 小程序 scheme)
    LINK,
}
