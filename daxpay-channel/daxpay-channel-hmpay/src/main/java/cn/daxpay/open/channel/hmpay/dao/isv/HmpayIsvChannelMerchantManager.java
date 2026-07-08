package cn.daxpay.open.channel.hmpay.dao.isv;

import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 河马付通道商户绑定 Manager
@Repository
public class HmpayIsvChannelMerchantManager extends BaseManager<HmpayIsvChannelMerchantMapper, HmpayIsvChannelMerchant> {

    /// 校验同一商户下杉德商户号不重复
    public boolean existsByMchNoAndMerchantNo(String mchNo, String merchantNo) {
        return lambdaQuery()
                .eq(HmpayIsvChannelMerchant::getMchNo, mchNo)
                .eq(HmpayIsvChannelMerchant::getMerchantNo, merchantNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<HmpayIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(HmpayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
