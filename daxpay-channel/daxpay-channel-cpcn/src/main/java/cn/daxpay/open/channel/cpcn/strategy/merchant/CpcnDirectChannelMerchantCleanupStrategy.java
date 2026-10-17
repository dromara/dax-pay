package cn.daxpay.open.channel.cpcn.strategy.merchant;

import cn.daxpay.open.channel.cpcn.dao.direct.CpcnDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 中金直连通道商户清理策略
///
/// 在通道商户删除时清理中金直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final CpcnDirectKeyConfigManager cpcnDirectKeyConfigManager;

    /// 对应产品: 中金
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.CPCN_PAY;
    }

    /// 清理指定通道商户号下中金直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        cpcnDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
