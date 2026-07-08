package cn.daxpay.open.channel.dougong.dao.isv;

import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 斗拱通道商户绑定 Manager
@Repository
public class DougongIsvChannelMerchantManager extends BaseManager<DougongIsvChannelMerchantMapper, DougongIsvChannelMerchant> {

    /// 校验同一商户下汇付商户号不重复
    public boolean existsByMchNoAndMerchantNo(String mchNo, String merchantNo) {
        return lambdaQuery()
                .eq(DougongIsvChannelMerchant::getMchNo, mchNo)
                .eq(DougongIsvChannelMerchant::getMerchantNo, merchantNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<DougongIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DougongIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
