package cn.daxpay.open.channel.longpay.strategy.merchant;

import cn.daxpay.open.channel.longpay.dao.direct.LongpayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 建行龙支付直连通道商户清理策略
///
/// 在通道商户删除时清理建行龙支付的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LongpayDirectKeyConfigManager longpayDirectKeyConfigManager;

    /// 对应产品: 建行龙支付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    /// 清理指定通道商户号下建行龙支付的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        longpayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
