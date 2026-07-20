package cn.daxpay.open.channel.wechat.cleanup.isv;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppCapability;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商通道商户清理策略
///
/// 在通道商户删除时清理微信服务商相关的扩展表（服务商扩展表、子商户应用、子商户应用能力、子商户应用授权配置）。
///
/// 注意：与 [cn.daxpay.open.channel.wechat.cleanup.direct.WechatDirectChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#WECHAT]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;
    private final WechatIsvMchAppCapabilityManager wechatIsvMchAppCapabilityManager;
    private final WechatIsvMchAppAuthConfigManager wechatIsvMchAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#WECHAT]，与直连共享）
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
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
