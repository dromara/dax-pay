package org.dromara.daxpay.channel.alipay.dao.isv;

import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 支付宝服务商通道商户绑定
///
@Repository
public class AlipayIsvChannelMerchantManager extends BaseManager<AlipayIsvChannelMerchantMapper, AlipayIsvChannelMerchant> {

    /// 校验同一服务商应用下是否已绑定该子商户
    public boolean existsByIsvAppIdAndAlipayUserId(Long isvAppId, String alipayUserId) {
        return lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getIsvAppId, isvAppId)
                .eq(AlipayIsvChannelMerchant::getAlipayUserId, alipayUserId)
                .exists();
    }

    /// 根据平台商户号查询服务商通道商户绑定(单绑定场景, 按创建时间升序取第一条)
    public Optional<AlipayIsvChannelMerchant> findByMchNo(String mchNo) {
        return Optional.ofNullable(lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getMchNo, mchNo)
                .orderByAsc(AlipayIsvChannelMerchant::getCreateTime)
                .orderByAsc(AlipayIsvChannelMerchant::getId)
                .last("limit 1")
                .one());
    }
}
