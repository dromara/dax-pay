package cn.daxpay.open.channel.alipay.cleanup.direct;

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
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 支付宝直连通道商户清理策略
///
/// 在通道商户删除时清理支付宝直连相关的所有扩展表（直连扩展表、应用、应用密钥、应用能力、应用授权配置）。
///
/// 注意：与 [cn.daxpay.open.channel.alipay.cleanup.isv.AlipayIsvChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#ALIPAY]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;
    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigManager alipayDirectAppKeyConfigManager;
    private final AlipayDirectAppCapabilityManager alipayDirectAppCapabilityManager;
    private final AlipayDirectAppAuthConfigManager alipayDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#ALIPAY]，与服务商共享）
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
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
