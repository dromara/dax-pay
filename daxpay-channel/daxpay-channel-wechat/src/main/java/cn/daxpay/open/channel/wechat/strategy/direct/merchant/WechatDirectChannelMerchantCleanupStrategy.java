package cn.daxpay.open.channel.wechat.strategy.direct.merchant;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectKeyConfigManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppCapability;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连通道商户清理策略
///
/// 在通道商户删除时清理微信直连相关的所有扩展表（直连扩展表、应用、应用密钥、应用能力、应用授权配置）。
///
/// 与 [cn.daxpay.open.channel.wechat.strategy.isv.merchant.WechatIsvChannelMerchantCleanupStrategy]
/// 分属不同 product(`WECHAT_PAY` vs `WECHAT_ISV`), 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct]
/// 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectKeyConfigManager wechatDirectAppKeyConfigManager;
    private final WechatDirectAppCapabilityManager wechatDirectAppCapabilityManager;
    private final WechatDirectAppAuthConfigManager wechatDirectAppAuthConfigManager;

    /// 对应产品: 微信支付直连
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    /// 清理指定通道商户号下微信直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatDirectChannelMerchantManager.deleteByField(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo);
        wechatDirectAppManager.deleteByField(WechatDirectApp::getChannelMchNo, channelMchNo);
        wechatDirectAppKeyConfigManager.deleteByField(WechatDirectKeyConfig::getChannelMchNo, channelMchNo);
        wechatDirectAppCapabilityManager.deleteByField(WechatDirectAppCapability::getChannelMchNo, channelMchNo);
        wechatDirectAppAuthConfigManager.deleteByField(WechatDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }
}
