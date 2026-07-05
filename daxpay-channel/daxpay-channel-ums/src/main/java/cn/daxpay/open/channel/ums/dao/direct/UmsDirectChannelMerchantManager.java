package cn.daxpay.open.channel.ums.dao.direct;

import cn.daxpay.open.channel.ums.entity.direct.UmsDirectChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 银联商务直连通道商户绑定
@Repository
public class UmsDirectChannelMerchantManager extends BaseManager<UmsDirectChannelMerchantMapper, UmsDirectChannelMerchant> {

    /// 根据通道商户号查询
    public Optional<UmsDirectChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(UmsDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
