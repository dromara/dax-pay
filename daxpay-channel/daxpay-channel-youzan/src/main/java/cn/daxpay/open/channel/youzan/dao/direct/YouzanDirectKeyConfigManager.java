package cn.daxpay.open.channel.youzan.dao.direct;

import cn.daxpay.open.channel.youzan.entity.direct.YouzanDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 有赞直连密钥配置
@Repository
public class YouzanDirectKeyConfigManager extends BaseManager<YouzanDirectKeyConfigMapper, YouzanDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<YouzanDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(YouzanDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<YouzanDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(YouzanDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(YouzanDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(YouzanDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
