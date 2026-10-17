package cn.daxpay.open.channel.sand.strategy.merchant;

import cn.daxpay.open.channel.sand.dao.direct.SandDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 杉德直连通道商户清理策略
///
/// 在通道商户删除时清理杉德直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final SandDirectKeyConfigManager sandDirectKeyConfigManager;

    /// 对应产品: 杉德
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SAND_PAY;
    }

    /// 清理指定通道商户号下杉德直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        sandDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
