package cn.daxpay.open.channel.douyin.cleanup.direct;

import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppAuthConfigManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppCapabilityManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectKeyConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppCapability;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连通道商户清理策略
///
/// 在通道商户删除时清理抖音直连相关的所有扩展表（直连扩展表、应用、应用密钥、应用能力、应用授权配置）。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;
    private final DouyinDirectAppManager douyinDirectAppManager;
    private final DouyinDirectKeyConfigManager douyinDirectAppKeyConfigManager;
    private final DouyinDirectAppCapabilityManager douyinDirectAppCapabilityManager;
    private final DouyinDirectAppAuthConfigManager douyinDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#DOUYIN]）
    @Override
    public String getChannel() {
        return ChannelEnum.DOUYIN.getCode();
    }

    /// 清理指定通道商户号下抖音直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        douyinDirectChannelMerchantManager.deleteByField(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo);
        douyinDirectAppManager.deleteByField(DouyinDirectApp::getChannelMchNo, channelMchNo);
        douyinDirectAppKeyConfigManager.deleteByField(DouyinDirectKeyConfig::getChannelMchNo, channelMchNo);
        douyinDirectAppCapabilityManager.deleteByField(DouyinDirectAppCapability::getChannelMchNo, channelMchNo);
        douyinDirectAppAuthConfigManager.deleteByField(DouyinDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }
}
