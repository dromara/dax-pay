package cn.daxpay.open.payment.merchant.service.channel;

/// # 通道商户扩展数据清理策略
///
/// 用于通道商户删除时的级联清理：各通道子模块（daxpay-channel-*）实现此策略，
/// 在主表 `mch_channel_merchant` 删除时同步清理通道私有数据（通道扩展表、密钥配置等）。
///
/// 设计要点：
/// - 主应用通过 [ChannelMerchantCleanupStrategyFactory] 按 `channel` 反查实现，未实现的通道跳过；
/// - 跳过的通道会留下孤儿数据，但主表已删、路由不再命中，对业务无影响；
/// - 实现类使用 `@Service`/`@Component` 注册到 Spring 容器，由工厂通过
///   [cn.hutool.extra.spring.SpringUtil#getBeansOfType] 统一收集；
/// - 同一 channel 允许多个实现（如 `ALIPAY` 同时存在直连与服务商两条子商户绑定，
///   `HUIFU` 同时对应 adapay 与 dougong 两个子模块），工厂按 channel 过滤后遍历调用，互不影响。
///
/// 命名约定：实现类命名为 `{Channel}{Mode}ChannelMerchantCleanupStrategy`，
/// 放在各通道子模块的 `cleanup/{direct|isv}/` 子包下，与支付策略（`strategy/`）子域物理隔离。
///
/// @see ChannelMerchantCleanupStrategyFactory
public interface ChannelMerchantCleanupStrategy {

    /// 该实现所属的通道编码
    ///
    /// 返回值应与 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum] 的 code 一致，
    /// 工厂按 `channel` 过滤后定位实现。同 channel 多个实现时全部调用。
    String getChannel();

    /// 清理指定通道商户的扩展数据
    ///
    /// 仅清理通道私有数据（扩展表 + KeyConfig 等），通用主表 `mch_channel_merchant` 由主应用负责删除。
    /// 实现应保证幂等：同一 channelMchNo 重复调用不应产生副作用。
    ///
    /// @param channelMchNo 通道商户号（平台侧生成的全局唯一号）
    void deleteByChannelMchNo(String channelMchNo);
}
