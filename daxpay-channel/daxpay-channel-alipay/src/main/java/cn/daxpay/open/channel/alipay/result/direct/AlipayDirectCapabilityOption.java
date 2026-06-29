package cn.daxpay.open.channel.alipay.result.direct;

/// # 支付能力候选项
///
/// 用于展示支付宝直连产品支持的支付能力编码及其国际化名称。
///
public record AlipayDirectCapabilityOption(String code, String name) {
}
