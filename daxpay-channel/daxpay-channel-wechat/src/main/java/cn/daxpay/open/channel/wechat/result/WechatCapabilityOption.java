package cn.daxpay.open.channel.wechat.result;

/// # 微信支付能力候选项
///
/// 用于展示微信产品(直连/服务商)支持的支付能力编码及其国际化名称。
/// 直连与服务商共用本结构。
///
public record WechatCapabilityOption(String code, String name) {
}
