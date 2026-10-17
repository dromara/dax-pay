package cn.daxpay.open.channel.helipay.dao;

import cn.daxpay.open.channel.helipay.entity.HelipayKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 合利宝通道密钥配置
@Repository
public class HelipayKeyConfigManager extends BaseManager<HelipayKeyConfigMapper, HelipayKeyConfig> {

    /// 根据通道商户号查询
    public Optional<HelipayKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(HelipayKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
