package cn.daxpay.open.channel.stripe.strategy.merchant;

import cn.daxpay.open.channel.stripe.dao.StripeChannelMerchantManager;
import cn.daxpay.open.channel.stripe.dao.StripeKeyConfigManager;
import cn.daxpay.open.channel.stripe.entity.StripeChannelMerchant;
import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Stripe 通道商户清理策略
///
/// 在通道商户删除时清理 Stripe 相关的扩展数据：
/// - 通道商户绑定(stripe_channel_merchant)
/// - 密钥配置(stripe_key_config)
///
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final StripeChannelMerchantManager stripeChannelMerchantManager;
    private final StripeKeyConfigManager stripeKeyConfigManager;

    /// 对应产品: Stripe 支付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.STRIPE_PAY;
    }

    /// 清理指定通道商户号下 Stripe 的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        stripeChannelMerchantManager.deleteByField(StripeChannelMerchant::getChannelMchNo, channelMchNo);
        stripeKeyConfigManager.deleteByField(StripeKeyConfig::getChannelMchNo, channelMchNo);
    }
}
