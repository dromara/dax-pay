package cn.daxpay.open.channel.epaylinks.strategy.merchant;

import cn.daxpay.open.channel.epaylinks.dao.direct.EpaylinksDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易票联直连通道商户清理策略
///
/// 在通道商户删除时清理易票联直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final EpaylinksDirectKeyConfigManager epaylinksDirectKeyConfigManager;

    /// 对应产品: 易票联
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    /// 清理指定通道商户号下易票联直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        epaylinksDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
