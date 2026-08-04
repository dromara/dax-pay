package cn.daxpay.open.channel.stripe.service;

import cn.daxpay.open.channel.stripe.convert.StripeKeyConfigConvert;
import cn.daxpay.open.channel.stripe.dao.StripeChannelMerchantManager;
import cn.daxpay.open.channel.stripe.dao.StripeKeyConfigManager;
import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import cn.daxpay.open.channel.stripe.param.StripeKeyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Stripe 密钥配置
///
/// 管理商户维度的密钥配置，查询时不存在则创建默认记录，保存时合并敏感字段。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeKeyConfigService {

    private final StripeKeyConfigManager stripeKeyConfigManager;
    private final StripeChannelMerchantManager stripeChannelMerchantManager;

    /// 根据通道商户号查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public StripeKeyConfig findByChannelMchNo(String channelMchNo) {
        var existing = stripeKeyConfigManager.findByChannelMchNo(channelMchNo);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new StripeKeyConfig()
                .setChannelMchNo(channelMchNo);
        // 查询通道商户记录填充商户号
        stripeChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
        stripeKeyConfigManager.save(config);
        return config;
    }

    /// 保存密钥配置(更新)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段(secretKey/publishableKey/webhookSecret);
    /// mchNo/channelMchNo 为不可变身份字段(实体 FieldStrategy.NEVER), 由 findByChannelMchNo 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(StripeKeyConfigParam param) {
        var config = this.findByChannelMchNo(param.getChannelMchNo());
        config.setMchNo(param.getMchNo());
        StripeKeyConfigConvert.CONVERT.copy(param, config);
        stripeKeyConfigManager.updateById(config);
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        stripeKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
