package cn.daxpay.open.payment.merchant.service.channel;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/// # 通道商户清理策略工厂
///
/// 模仿 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory] 的工具类风格：
/// - `@UtilityClass` 所有方法静态化；
/// - 通过 [SpringUtil#getBeansOfType] 在调用时实时查 Spring 容器（与 PaymentStrategyFactory 同构）；
/// - 按策略的 [ChannelMerchantCleanupStrategy#getChannel] 业务过滤，而非依赖包路径或 bean 名。
///
/// 与 PaymentStrategyFactory 的唯一结构差异：本工厂支持**同 channel 一对多**（如 `ALIPAY` 直连+服务商共享，
/// `HUIFU` 同时对应 adapay 与 dougong），用 `filter().forEach()` 遍历所有匹配实现；
/// PaymentStrategyFactory 则是按 product 一对一，用 `findFirst()`。
///
/// 未实现策略的通道跳过（孤儿数据不影响业务：主表已删、路由不再命中）。
@UtilityClass
public class ChannelMerchantCleanupStrategyFactory {

    /// 按通道清理指定通道商户的扩展数据
    ///
    /// 同 channel 存在多个实现时全部调用（如 `ALIPAY` 直连+服务商、`HUIFU` 的 adapay + dougong）。
    /// 未注册任何实现的 channel 静默跳过，不抛异常。
    ///
    /// @param channel     通道编码（来自 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum]）
    /// @param channelMchNo 通道商户号
    public void cleanup(String channel, String channelMchNo) {
        SpringUtil.getBeansOfType(ChannelMerchantCleanupStrategy.class)
                .values().stream()
                .filter(strategy -> strategy.getChannel().equals(channel))
                .forEach(strategy -> strategy.deleteByChannelMchNo(channelMchNo));
    }
}
