package cn.daxpay.open.channel.adapay.strategy.merchant;

import cn.daxpay.open.channel.adapay.dao.direct.AdapayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Adapay 直连通道商户清理策略
///
/// 在通道商户删除时清理 Adapay 直连的所有扩展数据（直连配置表）。
///
/// 与 [cn.daxpay.open.channel.dougong.strategy.merchant.DougongIsvChannelMerchantCleanupStrategy]
/// 分属不同 product(`ADA_PAY` vs `DOUGONG_PAY`), 共享同一通道 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum#HUIFU],
/// 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct] 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AdapayDirectKeyConfigManager adapayDirectKeyConfigManager;

    /// 对应产品: Adapay
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    /// 清理指定通道商户号下 Adapay 直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        adapayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
