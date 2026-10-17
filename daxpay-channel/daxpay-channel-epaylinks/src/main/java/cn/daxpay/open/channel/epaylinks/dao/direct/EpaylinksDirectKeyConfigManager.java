package cn.daxpay.open.channel.epaylinks.dao.direct;

import cn.daxpay.open.channel.epaylinks.entity.direct.EpaylinksDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易票联直连密钥配置
@Repository
public class EpaylinksDirectKeyConfigManager extends BaseManager<EpaylinksDirectKeyConfigMapper, EpaylinksDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<EpaylinksDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(EpaylinksDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<EpaylinksDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(EpaylinksDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(EpaylinksDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(EpaylinksDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
