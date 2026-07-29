package cn.daxpay.open.channel.douyin.strategy.merchant;

import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectKeyConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.payment.douyin.dao.channel.DyChannelAppCapabilityManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连通道商户清理策略
///
/// 在通道商户删除时清理抖音直连相关的扩展数据：
/// - 通道商户绑定(douyin_direct_channel_merchant)、密钥配置(douyin_direct_key_config)
/// - 通道能力绑定(dy_channel_app_capability, 释放对商户/平台应用的引用)
///
/// 注意：商户/平台级应用主数据(dy_mch_app / dy_platform_app)不在此清理 —— 它们可被多个通道商户引用，
/// 归属商户/平台级生命周期管理。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;
    private final DouyinDirectKeyConfigManager douyinDirectAppKeyConfigManager;
    private final DyChannelAppCapabilityManager dyChannelAppCapabilityManager;

    /// 对应产品: 抖音支付直连
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    /// 清理指定通道商户号下抖音直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        douyinDirectChannelMerchantManager.deleteByField(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo);
        douyinDirectAppKeyConfigManager.deleteByField(DouyinDirectKeyConfig::getChannelMchNo, channelMchNo);
        // 清理通道能力绑定(释放对商户/平台抖音应用的引用)
        dyChannelAppCapabilityManager.deleteByChannelMchNo(channelMchNo);
    }
}
