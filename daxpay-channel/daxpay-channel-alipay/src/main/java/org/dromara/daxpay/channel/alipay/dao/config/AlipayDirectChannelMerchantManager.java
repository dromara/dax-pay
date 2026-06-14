package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayDirectChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository
public class AlipayDirectChannelMerchantManager extends BaseManager<AlipayDirectChannelMerchantMapper, AlipayDirectChannelMerchant> {

    /// 校验同一商户下是否已绑定该支付宝商户PID
    public boolean existsByMchNoAndAlipayUserId(String mchNo, String alipayUserId) {
        return lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getMchNo, mchNo)
                .eq(AlipayDirectChannelMerchant::getAlipayUserId, alipayUserId)
                .exists();
    }
}
