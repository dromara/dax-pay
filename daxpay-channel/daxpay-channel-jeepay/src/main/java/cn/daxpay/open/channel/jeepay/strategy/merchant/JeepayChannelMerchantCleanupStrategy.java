package cn.daxpay.open.channel.jeepay.strategy.merchant;

import cn.daxpay.open.channel.jeepay.dao.JeepayKeyConfigManager;
import cn.daxpay.open.channel.jeepay.entity.JeepayKeyConfig;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Jeepay通道商户清理策略
///
/// 在通道商户删除时清理 Jeepay 的所有扩展数据(jee_pay_key_config)。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final JeepayKeyConfigManager jeepayKeyConfigManager;

    /// 对应产品: Jeepay 计全支付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    /// 清理指定通道商户号下 Jeepay 的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        jeepayKeyConfigManager.deleteByField(JeepayKeyConfig::getChannelMchNo, channelMchNo);
    }
}
