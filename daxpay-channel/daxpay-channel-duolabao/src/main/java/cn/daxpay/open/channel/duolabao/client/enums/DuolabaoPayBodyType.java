package cn.daxpay.open.channel.duolabao.client.enums;

/// # 哆啦宝支付内容类型(主应用侧)
///
/// 与子应用 DuolabaoPayBodyType 对齐, 用于映射平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
public enum DuolabaoPayBodyType {
    /// 二维码内容(聚合码链接)
    QR_CODE,
    /// 跳转链接(云闪付 H5 收银页)
    LINK,
    /// JSAPI 调起参数(支付宝交易号串 / 微信调起参数 JSON)
    JSAPI;
}
