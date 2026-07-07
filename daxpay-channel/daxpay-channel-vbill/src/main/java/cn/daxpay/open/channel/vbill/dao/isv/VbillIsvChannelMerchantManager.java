package cn.daxpay.open.channel.vbill.dao.isv;

import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 随行付通道商户绑定 Manager
@Repository
public class VbillIsvChannelMerchantManager extends BaseManager<VbillIsvChannelMerchantMapper, VbillIsvChannelMerchant> {

    /// 校验同一商户号下天阙商户号不重复
    public boolean existsByMchNoAndVbillMchNo(String mchNo, String vbillMchNo) {
        return lambdaQuery()
                .eq(VbillIsvChannelMerchant::getMchNo, mchNo)
                .eq(VbillIsvChannelMerchant::getVbillMchNo, vbillMchNo)
                .exists();
    }

    /// 根据通道商户号查询
    public Optional<VbillIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(VbillIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
