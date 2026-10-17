package cn.daxpay.open.channel.shouqianba.strategy.merchant;

import cn.daxpay.open.channel.shouqianba.dao.direct.ShouqianbaDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 收钱吧直连通道商户清理策略
///
/// 在通道商户删除时清理收钱吧直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final ShouqianbaDirectKeyConfigManager shouqianbaDirectKeyConfigManager;

    /// 对应产品: 收钱吧
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    /// 清理指定通道商户号下收钱吧直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        shouqianbaDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
