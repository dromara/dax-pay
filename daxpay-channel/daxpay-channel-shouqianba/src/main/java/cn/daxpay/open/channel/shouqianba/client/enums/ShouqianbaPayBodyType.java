package cn.daxpay.open.channel.shouqianba.client.enums;

/// # 收钱吧支付内容类型(主应用侧)
///
/// 与子应用 ShouqianbaPayBodyType 对齐。
public enum ShouqianbaPayBodyType {
    /// 二维码内容(precreate 返回的 qr_code)
    QR_CODE,

    /// 跳转链接(wap api pro 收银台 URL, 微信内/H5 场景)
    LINK;
}
