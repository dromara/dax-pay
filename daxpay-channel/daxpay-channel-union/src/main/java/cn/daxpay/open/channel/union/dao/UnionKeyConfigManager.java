package cn.daxpay.open.channel.union.dao;

import cn.daxpay.open.channel.union.entity.UnionKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 云闪付密钥配置
@Repository
public class UnionKeyConfigManager extends BaseManager<UnionKeyConfigMapper, UnionKeyConfig> {

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<UnionKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(UnionKeyConfig::getChannelMchNo, channelMchNo)
                .eq(UnionKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(UnionKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
