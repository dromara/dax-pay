package cn.daxpay.open.channel.lakala.dao.isv;

import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 拉卡拉通道商户绑定
///
@Repository
public class LakalaIsvChannelMerchantManager extends BaseManager<LakalaIsvChannelMerchantMapper, LakalaIsvChannelMerchant> {

    /// 校验同一商户下拉卡拉商户号不重复
    public boolean existsByMchNoAndLakalaMchNo(String mchNo, String lakalaMchNo) {
        return lambdaQuery()
                .eq(LakalaIsvChannelMerchant::getMchNo, mchNo)
                .eq(LakalaIsvChannelMerchant::getLakalaMchNo, lakalaMchNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<LakalaIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(LakalaIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
