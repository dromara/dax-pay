package cn.daxpay.open.channel.sand.dao.direct;

import cn.daxpay.open.channel.sand.entity.direct.SandDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 杉德直连密钥配置
@Repository
public class SandDirectKeyConfigManager extends BaseManager<SandDirectKeyConfigMapper, SandDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<SandDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(SandDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<SandDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(SandDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(SandDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(SandDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
