package cn.daxpay.open.channel.stripe.dao;

import cn.daxpay.open.channel.stripe.entity.StripeChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Stripe 通道商户绑定
///
@Repository
public class StripeChannelMerchantManager extends BaseManager<StripeChannelMerchantMapper, StripeChannelMerchant> {

    /// 校验同一商户下 Stripe 账户不重复
    public boolean existsByMchNoAndAccountId(String mchNo, String accountId) {
        return lambdaQuery()
                .eq(StripeChannelMerchant::getMchNo, mchNo)
                .eq(StripeChannelMerchant::getAccountId, accountId)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<StripeChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(StripeChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(StripeChannelMerchant::getChannelMchNo, channelMchNo)
                .remove();
    }
}
