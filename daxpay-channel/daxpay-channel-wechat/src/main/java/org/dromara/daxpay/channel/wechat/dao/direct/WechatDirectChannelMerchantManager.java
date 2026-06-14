package org.dromara.daxpay.channel.wechat.dao.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 微信直连通道商户绑定
///
@Repository
public class WechatDirectChannelMerchantManager extends BaseManager<WechatDirectChannelMerchantMapper, WechatDirectChannelMerchant> {

    /// 校验同一商户下微信直连商户号不重复
    public boolean existsByMchNoAndWxMchId(String mchNo, String wxMchId) {
        return lambdaQuery()
                .eq(WechatDirectChannelMerchant::getMchNo, mchNo)
                .eq(WechatDirectChannelMerchant::getWxMchId, wxMchId)
                .exists();
    }
}
