package cn.daxpay.open.channel.wechat.cleanup.direct;

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
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连通道商户清理策略
///
/// 在通道商户删除时清理微信直连相关的所有扩展表（直连扩展表、应用、应用密钥、应用能力、应用授权配置）。
///
/// 注意：与 [cn.daxpay.open.channel.wechat.cleanup.isv.WechatIsvChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#WECHAT]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectKeyConfigManager wechatDirectAppKeyConfigManager;
    private final WechatDirectAppCapabilityManager wechatDirectAppCapabilityManager;
    private final WechatDirectAppAuthConfigManager wechatDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#WECHAT]，与服务商共享）
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
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
