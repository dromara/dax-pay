package cn.daxpay.open.channel.adapay.dao.direct;

import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Adapay 直连密钥配置 Manager
@Repository
public class AdapayDirectKeyConfigManager extends BaseManager<AdapayDirectKeyConfigMapper, AdapayDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<AdapayDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(AdapayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(AdapayDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
