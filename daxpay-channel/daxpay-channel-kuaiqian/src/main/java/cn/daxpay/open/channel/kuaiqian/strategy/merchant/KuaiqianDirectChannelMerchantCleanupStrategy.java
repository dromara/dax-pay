package cn.daxpay.open.channel.kuaiqian.strategy.merchant;

import cn.daxpay.open.channel.kuaiqian.dao.direct.KuaiqianDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 快钱直连通道商户清理策略
///
/// 在通道商户删除时清理快钱直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final KuaiqianDirectKeyConfigManager kuaiqianDirectKeyConfigManager;

    /// 对应产品: 快钱
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.KUAIQIAN_PAY;
    }

    /// 清理指定通道商户号下快钱直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        kuaiqianDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
