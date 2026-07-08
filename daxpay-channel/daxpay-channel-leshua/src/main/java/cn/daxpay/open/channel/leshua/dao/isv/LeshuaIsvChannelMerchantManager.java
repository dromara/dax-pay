package cn.daxpay.open.channel.leshua.dao.isv;

import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 乐刷通道商户绑定
///
@Repository
public class LeshuaIsvChannelMerchantManager extends BaseManager<LeshuaIsvChannelMerchantMapper, LeshuaIsvChannelMerchant> {

    /// 校验同一商户下乐刷商户号不重复
    public boolean existsByMchNoAndLsMchNo(String mchNo, String lsMchNo) {
        return lambdaQuery()
                .eq(LeshuaIsvChannelMerchant::getMchNo, mchNo)
                .eq(LeshuaIsvChannelMerchant::getLsMchNo, lsMchNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<LeshuaIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(LeshuaIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
