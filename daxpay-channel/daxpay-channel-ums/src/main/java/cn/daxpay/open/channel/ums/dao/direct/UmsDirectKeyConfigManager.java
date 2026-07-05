package cn.daxpay.open.channel.ums.dao.direct;

import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 银联商务直连密钥配置
@Repository
public class UmsDirectKeyConfigManager extends BaseManager<UmsDirectKeyConfigMapper, UmsDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<UmsDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(UmsDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(UmsDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
