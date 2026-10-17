package cn.daxpay.open.channel.jlpay.dao.direct;

import cn.daxpay.open.channel.jlpay.entity.direct.JlpayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 嘉联直连密钥配置
@Repository
public class JlpayDirectKeyConfigManager extends BaseManager<JlpayDirectKeyConfigMapper, JlpayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<JlpayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(JlpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<JlpayDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(JlpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(JlpayDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(JlpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
