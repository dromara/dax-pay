package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 支付宝直连通道商户绑定
///
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
