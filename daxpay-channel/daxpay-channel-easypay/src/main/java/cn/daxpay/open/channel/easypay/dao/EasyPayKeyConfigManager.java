package cn.daxpay.open.channel.easypay.dao;

import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易支付通道密钥配置
@Repository
public class EasyPayKeyConfigManager extends BaseManager<EasyPayKeyConfigMapper, EasyPayKeyConfig> {

    /// 根据通道商户号查询
    public Optional<EasyPayKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(EasyPayKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
