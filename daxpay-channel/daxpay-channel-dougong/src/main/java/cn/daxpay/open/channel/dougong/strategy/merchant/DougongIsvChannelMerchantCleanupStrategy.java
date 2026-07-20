package cn.daxpay.open.channel.dougong.strategy.merchant;

import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvChannelMerchantManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 斗拱通道商户清理策略
///
/// 在通道商户删除时清理斗拱的所有扩展数据。
///
/// 与 [cn.daxpay.open.channel.adapay.strategy.merchant.AdapayDirectChannelMerchantCleanupStrategy]
/// 分属不同 product(`DOUGONG_PAY` vs `ADA_PAY`), 共享同一通道 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum#HUIFU],
/// 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct] 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final DougongIsvChannelMerchantManager dougongIsvChannelMerchantManager;

    /// 对应产品: 斗拱
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    /// 清理指定通道商户号下斗拱的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        dougongIsvChannelMerchantManager.deleteByField(DougongIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
