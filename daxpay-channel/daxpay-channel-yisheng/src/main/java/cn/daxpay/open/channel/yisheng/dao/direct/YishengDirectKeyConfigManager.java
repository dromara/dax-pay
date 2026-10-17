package cn.daxpay.open.channel.yisheng.dao.direct;

import cn.daxpay.open.channel.yisheng.entity.direct.YishengDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易生直连密钥配置
@Repository
public class YishengDirectKeyConfigManager extends BaseManager<YishengDirectKeyConfigMapper, YishengDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<YishengDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(YishengDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<YishengDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(YishengDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(YishengDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(YishengDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
