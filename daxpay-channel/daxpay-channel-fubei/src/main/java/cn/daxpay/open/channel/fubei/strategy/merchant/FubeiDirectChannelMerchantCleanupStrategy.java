package cn.daxpay.open.channel.fubei.strategy.merchant;

import cn.daxpay.open.channel.fubei.dao.direct.FubeiDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 付呗直连通道商户清理策略
///
/// 在通道商户删除时清理付呗直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FubeiDirectKeyConfigManager fubeiDirectKeyConfigManager;

    /// 对应产品: 付呗
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    /// 清理指定通道商户号下付呗直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fubeiDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
