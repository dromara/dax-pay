package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    /// 根据通道商户号查询
    public Optional<WechatDirectChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
