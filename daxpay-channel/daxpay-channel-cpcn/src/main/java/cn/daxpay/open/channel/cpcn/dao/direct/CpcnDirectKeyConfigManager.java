package cn.daxpay.open.channel.cpcn.dao.direct;

import cn.daxpay.open.channel.cpcn.entity.direct.CpcnDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 中金直连密钥配置
@Repository
public class CpcnDirectKeyConfigManager extends BaseManager<CpcnDirectKeyConfigMapper, CpcnDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<CpcnDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(CpcnDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<CpcnDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(CpcnDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(CpcnDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(CpcnDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
