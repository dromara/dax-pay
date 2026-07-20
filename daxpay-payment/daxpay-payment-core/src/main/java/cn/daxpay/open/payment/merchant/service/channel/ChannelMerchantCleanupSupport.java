package cn.daxpay.open.payment.merchant.service.channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 通道商户扩展数据清理调度器
///
/// 收集所有 [ChannelMerchantCleanupService] SPI 实现，按 `channel` 建索引。
/// 删除通道商户时由主应用按 `product → channel` 反查后调用对应实现，清理通道私有数据。
///
/// 设计要点：
/// - **同 channel 允许多个实现**：某些通道（如 `huifu` 同时对应 adapay 与 dougong 两个子模块）
///   需要分别清理各自的扩展表，因此按 `channel` 聚合为 List，cleanup 时遍历调用。
/// - 未实现 SPI 的通道跳过（孤儿数据不影响业务：主表已删、路由不再命中）。
///
/// 注意：本类只在 [ChannelMerchantService#delete] 路径中调用，**不**用于商户级联删除（避免事务过深、循环依赖）。
@Slf4j
@Service
public class ChannelMerchantCleanupSupport {

    private final Map<String, List<ChannelMerchantCleanupService>> cleanupByChannel;

    public ChannelMerchantCleanupSupport(List<ChannelMerchantCleanupService> all) {
        this.cleanupByChannel = all.stream()
                .collect(Collectors.groupingBy(ChannelMerchantCleanupService::getChannel));
        log.info("通道商户扩展清理器已加载: {}", cleanupByChannel);
    }

    /// 按通道清理指定通道商户的扩展数据
    ///
    /// 同 channel 存在多个实现时全部调用（如 huifu 的 adapay + dougong）。
    ///
    /// @param channel     通道编码（来自 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum]）
    /// @param channelMchNo 通道商户号
    public void cleanup(String channel, String channelMchNo) {
        var services = cleanupByChannel.get(channel);
        if (services == null || services.isEmpty()) {
            // 通道未实现 SPI：跳过，留孤儿数据
            log.debug("通道 {} 未实现 ChannelMerchantCleanupService, 跳过清理 channelMchNo={}", channel, channelMchNo);
            return;
        }
        for (var svc : services) {
            svc.deleteByChannelMchNo(channelMchNo);
        }
    }
}
