package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    /// 根据通道商户号查询
    public Optional<WechatIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
