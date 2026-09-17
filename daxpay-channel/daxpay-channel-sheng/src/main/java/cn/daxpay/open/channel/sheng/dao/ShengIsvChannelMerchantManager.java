package cn.daxpay.open.channel.sheng.dao;

import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 盛付通服务商通道商户绑定
@Repository
public class ShengIsvChannelMerchantManager extends BaseManager<ShengIsvChannelMerchantMapper, ShengIsvChannelMerchant> {

    /// 根据通道商户号查询
    public Optional<ShengIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(ShengIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
