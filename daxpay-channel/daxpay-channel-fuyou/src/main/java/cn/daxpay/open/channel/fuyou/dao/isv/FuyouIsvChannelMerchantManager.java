package cn.daxpay.open.channel.fuyou.dao.isv;

import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 富友通道商户绑定 Manager
@Slf4j
@Service
public class FuyouIsvChannelMerchantManager extends BaseManager<FuyouIsvChannelMerchantMapper, FuyouIsvChannelMerchant> {

    /// 根据通道商户号查询
    public Optional<FuyouIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 校验同一商户号下富友商户号是否重复
    public boolean existsByMchNoAndFuyouMchNo(String mchNo, String fuyouMchNo) {
        return lambdaQuery()
                .eq(FuyouIsvChannelMerchant::getMchNo, mchNo)
                .eq(FuyouIsvChannelMerchant::getFuyouMchNo, fuyouMchNo)
                .exists();
    }
}
