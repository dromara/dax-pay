package cn.daxpay.open.payment.merchant.service.channel;

/// # 通道商户扩展数据清理扩展点
///
/// 用于通道商户删除时的级联清理：各通道子模块（daxpay-channel-*）实现此接口，
/// 在主表 `mch_channel_merchant` 删除时同步清理通道私有数据（通道扩展表、密钥配置等）。
///
/// 设计要点：
/// - 主应用通过 [ChannelMerchantCleanupSupport] 按 `channel` 反查实现，未实现的通道跳过；
/// - 跳过的通道会留下孤儿数据，但主表已删、路由不再命中，对业务无影响；
/// - 实现类应使用 `@Service`/`@Component` 注册到 Spring 容器，主应用统一收集。
///
/// @see ChannelMerchantCleanupSupport
public interface ChannelMerchantCleanupService {

    /// 该实现所属的通道编码
    ///
    /// 返回值应与 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum] 的 code 一致，
    /// 主应用按 `product → channel` 反查后定位实现。
    String getChannel();

    /// 清理指定通道商户的扩展数据
    ///
    /// 仅清理通道私有数据（扩展表 + KeyConfig 等），通用主表 `mch_channel_merchant` 由主应用负责删除。
    /// 实现应保证幂等：同一 channelMchNo 重复调用不应产生副作用。
    ///
    /// @param channelMchNo 通道商户号（平台侧生成的全局唯一号）
    void deleteByChannelMchNo(String channelMchNo);
}
