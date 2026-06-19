package cn.daxpay.open.payment.merchant.service.route.model;

import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;

/// # 通道路由命中结果
///
/// 匹配成功后得到的通道、方式、产品及命中来源 ID，供填充 PayParam。
///
/// @param channel     支付通道编码
/// @param method      支付方式编码
/// @param product     支付产品编码（可为空，由运行时解析）
/// @param hitRuleId   精细模式命中的规则 ID（预留字段，当前实现恒为 null）
/// @param hitConfigId 场景模式命中的场景配置 ID，基础模式为 null
public record RouteHit(String channel, String method, String product, Long hitRuleId, Long hitConfigId) {

    /// 由场景模式配置构造命中结果
    public static RouteHit fromScene(PayRouteSceneConfig config) {
        return new RouteHit(config.getChannel(), config.getMethod(), config.getProduct(), null, config.getId());
    }
}
