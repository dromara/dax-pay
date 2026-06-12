package org.dromara.daxpay.payment.channel.strategy;

import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;

/// # 通道商户策略
///
public abstract class AbsChannelMerchantStrategy implements OnbStrategy {

    /// 进件申请商户相关的数据处理
    public void mchApplyHandler(ChannelMerchant channelMerchant){

    }

    /// 手工新增通道商户相关数据处理
    public void mchCreatHandler(ChannelMerchant channelMerchant){

    }

}
