package cn.daxpay.open.payment.wx.result;

/// # 微信支付能力候选项
///
/// 用于展示平台微信应用可绑定的支付能力编码及其国际化名称;
/// defaultAppName/defaultWxAppId 为该能力在产品级平台默认绑中配置的应用摘要(未配置为 null),
/// 供通道商户绑定弹窗回显「服务商默认」选项当前生效的应用。
///
public record WxCapabilityOption(String code, String name, String defaultAppName, String defaultWxAppId) {
}
