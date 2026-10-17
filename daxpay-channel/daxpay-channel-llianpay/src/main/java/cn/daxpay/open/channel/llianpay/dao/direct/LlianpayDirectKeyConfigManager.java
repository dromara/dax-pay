package cn.daxpay.open.channel.llianpay.dao.direct;

import cn.daxpay.open.channel.llianpay.entity.direct.LlianpayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 连连支付直连密钥配置
@Repository
public class LlianpayDirectKeyConfigManager extends BaseManager<LlianpayDirectKeyConfigMapper, LlianpayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<LlianpayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(LlianpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<LlianpayDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(LlianpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(LlianpayDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(LlianpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
