package cn.daxpay.open.channel.fuyou.client.enums;

/// # 富友 H5 聚合收银台支付方式(主应用侧)
///
/// 对应富友「H5 聚合收银台」接口(token/order.fuiou)的 pay_type 取值组合,
/// 各方式均返回 token_url(收银台链接/小程序 scheme)。
public enum FuyouH5PayMethod {

    /// 支付宝扫码(经收银台): pay_type=AliPayJL
    ALIPAY_QR,
    /// 微信小程序: pay_type=WeAppletPay
    WECHAT_MINI,
    /// 云闪付 App(经收银台): pay_type=AppQuickPay
    UNION_QR,
}
