package cn.daxpay.open.payment.merchant.service.route.model;

/// # 通道路由命中结果
///
/// 匹配成功后得到的支付产品、通道商户及能力，供填充 NormalPayParam。
///
/// @param product      支付产品编码（由通道商户号派生）
/// @param channelMchNo 通道商户号（场景/基础模式命中的核心定位字段）
/// @param capability   支付能力编码（场景模式命中的能力声明；基础模式可为空）
public record RouteHit(String product, String channelMchNo, String capability) {

    /// 由场景模式配置构造命中结果（product 留空，由运行时按通道商户号派生）
    public static RouteHit fromScene(String channelMchNo, String capability) {
        return new RouteHit(null, channelMchNo, capability);
    }
}
