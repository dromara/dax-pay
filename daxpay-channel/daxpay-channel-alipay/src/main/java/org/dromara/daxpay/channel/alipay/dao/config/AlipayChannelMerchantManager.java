package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository
public class AlipayChannelMerchantManager extends BaseManager<AlipayChannelMerchantMapper, AlipayChannelMerchant> {

    /// 校验同一支付宝服务商应用下是否已绑定该子商户号
    public boolean existsByIsvAppIdAndAlipayUserId(String isvAppId, String alipayUserId) {
        return lambdaQuery()
                .eq(AlipayChannelMerchant::getIsvAppId, isvAppId)
                .eq(AlipayChannelMerchant::getAlipayUserId, alipayUserId)
                .exists();
    }
}
