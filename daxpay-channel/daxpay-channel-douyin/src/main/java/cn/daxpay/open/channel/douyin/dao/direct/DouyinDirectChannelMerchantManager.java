package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 抖音直连通道商户绑定
///
@Repository
public class DouyinDirectChannelMerchantManager extends BaseManager<DouyinDirectChannelMerchantMapper, DouyinDirectChannelMerchant> {

    /// 校验同一商户下抖音直连商户号不重复
    public boolean existsByMchNoAndDyMchId(String mchNo, String dyMchId) {
        return lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getMchNo, mchNo)
                .eq(DouyinDirectChannelMerchant::getDyMchId, dyMchId)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<DouyinDirectChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
