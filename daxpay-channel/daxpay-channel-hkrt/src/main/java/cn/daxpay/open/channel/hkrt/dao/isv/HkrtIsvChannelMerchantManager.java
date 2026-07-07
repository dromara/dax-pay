package cn.daxpay.open.channel.hkrt.dao.isv;

import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 海科融通通道商户绑定
///
@Repository
public class HkrtIsvChannelMerchantManager extends BaseManager<HkrtIsvChannelMerchantMapper, HkrtIsvChannelMerchant> {

    /// 校验同一商户下海科商户号不重复
    public boolean existsByMchNoAndMerchNo(String mchNo, String merchNo) {
        return lambdaQuery()
                .eq(HkrtIsvChannelMerchant::getMchNo, mchNo)
                .eq(HkrtIsvChannelMerchant::getMerchNo, merchNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<HkrtIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(HkrtIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
