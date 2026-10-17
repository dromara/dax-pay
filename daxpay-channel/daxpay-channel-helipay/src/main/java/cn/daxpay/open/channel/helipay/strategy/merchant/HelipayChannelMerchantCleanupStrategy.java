package cn.daxpay.open.channel.helipay.strategy.merchant;

import cn.daxpay.open.channel.helipay.dao.HelipayKeyConfigManager;
import cn.daxpay.open.channel.helipay.entity.HelipayKeyConfig;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 合利宝直连通道商户清理策略
///
/// 在通道商户删除时清理合利宝的所有扩展数据(helipay_key_config)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final HelipayKeyConfigManager helipayKeyConfigManager;

    /// 对应产品: 合利宝
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    /// 清理指定通道商户号下合利宝的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        helipayKeyConfigManager.deleteByField(HelipayKeyConfig::getChannelMchNo, channelMchNo);
    }
}
