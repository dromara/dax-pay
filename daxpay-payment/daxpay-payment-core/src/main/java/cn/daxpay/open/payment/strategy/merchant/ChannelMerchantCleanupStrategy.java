package cn.daxpay.open.payment.strategy.merchant;

import cn.daxpay.open.payment.strategy.PaymentStrategy;

/// # 通道商户扩展数据清理策略
///
/// 用于通道商户删除时的级联清理：各通道子模块（daxpay-channel-*）实现此策略，
/// 在主表 `mch_channel_merchant` 删除时同步清理通道私有数据（通道扩展表、密钥配置等）。
///
/// 设计要点：
/// - 继承 [PaymentStrategy], 通过 [PaymentStrategyFactory#findOptionallyByProduct] 按 `product` 反查实现,
///   与既有支付策略(Pay/Refund/Sync 等)共用同一套工厂体系, 形式完全对齐;
/// - 按 `product` 一对一查找, 区别于支付策略的"通道分组遍历"——每个通道商户记录只对应一个 product,
///   删除时只触发对应 product 的清理策略, 不影响同 channel 其他 product 的扩展数据;
/// - 未实现策略的 product 静默跳过([PaymentStrategyFactory#findOptionallyByProduct] 返回空 Optional),
///   留孤儿数据(主表已删、路由不再命中), 业务无影响;
/// - 实现类使用 `@Service`/`@Component` 注册到 Spring 容器, 由工厂通过
///   [cn.hutool.extra.spring.SpringUtil#getBeansOfType] 统一收集。
///
/// 命名约定：实现类命名为 `{Channel}{Mode}ChannelMerchantCleanupStrategy`,
/// 放在各通道子模块的 `strategy/direct|isv/merchant/`(alipay/wechat 三层)
/// 或 `strategy/merchant/`(其他 11 个通道二层)子包下,
/// 与支付策略(`strategy/direct|isv/{pay,refund,sync,product}/`)按业务子域物理隔离。
///
/// 特殊场景：同一通道多 product 共享同一份扩展表的(如 UMS 的 6 个支付方式 product
/// 共享 `ums_direct_key_config`), 用抽象父类封装共享删除逻辑 + N 个产品子类各自
/// `@Override getProduct()` 返回对应 [cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum]。
public interface ChannelMerchantCleanupStrategy extends PaymentStrategy {

    /// 清理指定通道商户的扩展数据
    ///
    /// 仅清理通道私有数据（扩展表 + KeyConfig 等），通用主表 `mch_channel_merchant` 由主应用负责删除。
    /// 实现应保证幂等：同一 channelMchNo 重复调用不应产生副作用。
    ///
    /// @param channelMchNo 通道商户号（平台侧生成的全局唯一号）
    void deleteByChannelMchNo(String channelMchNo);
}
