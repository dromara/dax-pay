package cn.daxpay.open.channel.sheng.dao;

import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 盛付通通道密钥配置
@Repository
public class ShengKeyConfigManager extends BaseManager<ShengKeyConfigMapper, ShengKeyConfig> {

    /// 根据通道商户号查询
    public Optional<ShengKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(ShengKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
