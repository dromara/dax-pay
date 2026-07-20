package cn.daxpay.open.channel.wechat.strategy.isv.merchant;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppCapability;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商通道商户清理策略
///
/// 在通道商户删除时清理微信服务商相关的扩展表（服务商扩展表、子商户应用、子商户应用能力、子商户应用授权配置）。
///
/// 与 [cn.daxpay.open.channel.wechat.strategy.direct.merchant.WechatDirectChannelMerchantCleanupStrategy]
/// 分属不同 product(`WECHAT_ISV` vs `WECHAT_PAY`), 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct]
/// 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;
    private final WechatIsvMchAppCapabilityManager wechatIsvMchAppCapabilityManager;
    private final WechatIsvMchAppAuthConfigManager wechatIsvMchAppAuthConfigManager;

    /// 对应产品: 微信支付服务商
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    /// 清理指定通道商户号下微信服务商的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatIsvChannelMerchantManager.deleteByField(WechatIsvChannelMerchant::getChannelMchNo, channelMchNo);
        wechatIsvMchAppManager.deleteByField(WechatIsvMchApp::getChannelMchNo, channelMchNo);
        wechatIsvMchAppCapabilityManager.deleteByField(WechatIsvMchAppCapability::getChannelMchNo, channelMchNo);
        wechatIsvMchAppAuthConfigManager.deleteByField(WechatIsvMchAppAuthConfig::getChannelMchNo, channelMchNo);
    }
}
