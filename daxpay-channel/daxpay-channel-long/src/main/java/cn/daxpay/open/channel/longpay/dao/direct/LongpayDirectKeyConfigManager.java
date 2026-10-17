package cn.daxpay.open.channel.longpay.dao.direct;

import cn.daxpay.open.channel.longpay.entity.direct.LongpayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 建行龙支付直连密钥配置
@Repository
public class LongpayDirectKeyConfigManager extends BaseManager<LongpayDirectKeyConfigMapper, LongpayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<LongpayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(LongpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<LongpayDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(LongpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(LongpayDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(LongpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
