package cn.daxpay.open.channel.adapay.cleanup.direct;

import cn.daxpay.open.channel.adapay.dao.direct.AdapayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Adapay 直连通道商户清理策略
///
/// 在通道商户删除时清理 Adapay 直连的所有扩展数据（直连配置表）。
///
/// 注意：Adapay 与 [cn.daxpay.open.channel.dougong.cleanup.isv.DougongIsvChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#HUIFU]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AdapayDirectKeyConfigManager adapayDirectKeyConfigManager;

    /// 通道编码（对应 [ChannelEnum#HUIFU]，与 Dougong 共享）
    @Override
    public String getChannel() {
        return ChannelEnum.HUIFU.getCode();
    }

    /// 清理指定通道商户号下 Adapay 直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        adapayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
