package cn.daxpay.open.channel.fubei.dao.direct;

import cn.daxpay.open.channel.fubei.entity.direct.FubeiDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 付呗直连密钥配置
@Repository
public class FubeiDirectKeyConfigManager extends BaseManager<FubeiDirectKeyConfigMapper, FubeiDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<FubeiDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(FubeiDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<FubeiDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(FubeiDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(FubeiDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(FubeiDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
