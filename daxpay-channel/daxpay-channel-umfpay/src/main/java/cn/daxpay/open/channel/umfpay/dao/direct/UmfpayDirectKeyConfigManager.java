package cn.daxpay.open.channel.umfpay.dao.direct;

import cn.daxpay.open.channel.umfpay.entity.direct.UmfpayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 联动优势直连密钥配置
@Repository
public class UmfpayDirectKeyConfigManager extends BaseManager<UmfpayDirectKeyConfigMapper, UmfpayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<UmfpayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(UmfpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<UmfpayDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(UmfpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(UmfpayDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(UmfpayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
