package org.dromara.daxpay.channel.alipay.dao.isv;

import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 支付宝服务商通道商户绑定
///
@Repository
public class AlipayIsvChannelMerchantManager extends BaseManager<AlipayIsvChannelMerchantMapper, AlipayIsvChannelMerchant> {

    /// 校验同一服务商应用下是否已绑定该子商户
    public boolean existsByAppIdAndAlipayUserId(Long appId, String alipayUserId) {
        return lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getAppId, appId)
                .eq(AlipayIsvChannelMerchant::getAlipayUserId, alipayUserId)
                .exists();
    }
}
