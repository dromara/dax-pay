package cn.daxpay.open.channel.yisheng.strategy.merchant;

import cn.daxpay.open.channel.yisheng.dao.direct.YishengDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易生直连通道商户清理策略
///
/// 在通道商户删除时清理易生直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final YishengDirectKeyConfigManager yishengDirectKeyConfigManager;

    /// 对应产品: 易生
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    /// 清理指定通道商户号下易生直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        yishengDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
