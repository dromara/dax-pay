package cn.daxpay.open.channel.jeepay.dao;

import cn.daxpay.open.channel.jeepay.entity.JeepayKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Jeepay通道密钥配置
@Repository
public class JeepayKeyConfigManager extends BaseManager<JeepayKeyConfigMapper, JeepayKeyConfig> {

    /// 根据通道商户号查询
    public Optional<JeepayKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(JeepayKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
