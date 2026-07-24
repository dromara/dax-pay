package cn.daxpay.open.payment.wx.result;

/// # 微信支付能力候选项
///
/// 用于展示平台微信应用可绑定的支付能力编码及其国际化名称。
///
public record WxCapabilityOption(String code, String name) {
}
