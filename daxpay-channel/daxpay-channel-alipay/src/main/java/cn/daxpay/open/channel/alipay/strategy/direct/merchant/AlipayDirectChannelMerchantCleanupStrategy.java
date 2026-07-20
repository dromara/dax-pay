package cn.daxpay.open.channel.alipay.strategy.direct.merchant;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppAuthConfigManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppCapabilityManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppKeyConfigManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 支付宝直连通道商户清理策略
///
/// 在通道商户删除时清理支付宝直连相关的所有扩展表（直连扩展表、应用、应用密钥、应用能力、应用授权配置）。
///
/// 与 [cn.daxpay.open.channel.alipay.strategy.isv.merchant.AlipayIsvChannelMerchantCleanupStrategy]
/// 分属不同 product(`ALIPAY` vs `ALIPAY_ISV`), 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct]
/// 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;
    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigManager alipayDirectAppKeyConfigManager;
    private final AlipayDirectAppCapabilityManager alipayDirectAppCapabilityManager;
    private final AlipayDirectAppAuthConfigManager alipayDirectAppAuthConfigManager;

    /// 对应产品: 支付宝直连
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    /// 清理指定通道商户号下支付宝直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        alipayDirectChannelMerchantManager.deleteByField(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo);
        alipayDirectAppManager.deleteByField(AlipayDirectApp::getChannelMchNo, channelMchNo);
        alipayDirectAppKeyConfigManager.deleteByField(AlipayDirectAppKeyConfig::getChannelMchNo, channelMchNo);
        alipayDirectAppCapabilityManager.deleteByField(AlipayDirectAppCapability::getChannelMchNo, channelMchNo);
        alipayDirectAppAuthConfigManager.deleteByField(AlipayDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }
}
