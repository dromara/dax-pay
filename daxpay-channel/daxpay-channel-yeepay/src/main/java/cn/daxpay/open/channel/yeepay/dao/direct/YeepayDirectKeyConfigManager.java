package cn.daxpay.open.channel.yeepay.dao.direct;

import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易宝直连密钥配置
@Repository
public class YeepayDirectKeyConfigManager extends BaseManager<YeepayDirectKeyConfigMapper, YeepayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<YeepayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(YeepayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<YeepayDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(YeepayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(YeepayDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(YeepayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
