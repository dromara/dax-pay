package cn.daxpay.open.channel.stripe.dao;

import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Stripe 密钥配置
///
/// 密钥配置数据访问管理器，提供按通道商户号查询和删除密钥配置的方法。
///
@Repository
public class StripeKeyConfigManager extends BaseManager<StripeKeyConfigMapper, StripeKeyConfig> {

    /// 根据通道商户号查询密钥配置
    public Optional<StripeKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(StripeKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(StripeKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
