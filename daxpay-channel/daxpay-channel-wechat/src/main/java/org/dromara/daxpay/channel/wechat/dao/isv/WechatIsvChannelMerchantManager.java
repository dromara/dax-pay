package org.dromara.daxpay.channel.wechat.dao.isv;

import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 微信服务商通道商户绑定
///
@Repository
public class WechatIsvChannelMerchantManager extends BaseManager<WechatIsvChannelMerchantMapper, WechatIsvChannelMerchant> {

    /// 校验同一商户下特约商户号不重复
    public boolean existsByMchNoAndSubMchId(String mchNo, String subMchId) {
        return lambdaQuery()
                .eq(WechatIsvChannelMerchant::getMchNo, mchNo)
                .eq(WechatIsvChannelMerchant::getSubMchId, subMchId)
                .exists();
    }
}
