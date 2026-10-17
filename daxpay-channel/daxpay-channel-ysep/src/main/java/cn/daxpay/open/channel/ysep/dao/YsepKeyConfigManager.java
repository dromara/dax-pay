package cn.daxpay.open.channel.ysep.dao;

import cn.daxpay.open.channel.ysep.entity.YsepKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 银盛通道密钥配置 Manager
@Repository
public class YsepKeyConfigManager extends BaseManager<YsepKeyConfigMapper, YsepKeyConfig> {

    /// 根据通道商户号查询(通道商户号全局唯一, 产品由行内 product 字段表达)
    public Optional<YsepKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(YsepKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 按通道商户号删除(通道商户删除时清理扩展数据)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(YsepKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
