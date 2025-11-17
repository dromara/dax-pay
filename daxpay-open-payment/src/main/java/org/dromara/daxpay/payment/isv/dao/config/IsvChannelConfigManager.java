package org.dromara.daxpay.payment.isv.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 服务商通道配置
 * @author xxm
 * @since 2024/10/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvChannelConfigManager extends BaseManager<IsvChannelConfigMapper, IsvChannelConfig> {

    /**
     * 根据服务商号查询
     */
    public List<IsvChannelConfig> findByAppId(String isvNo) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(IsvChannelConfig::getIsvNo, isvNo)
                .list();
    }
    /**
     * 根据服务商号查询启用的配置
     */
    public List<IsvChannelConfig> findEnableByIsvNo(String isvNo) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(IsvChannelConfig::getEnable, true)
                .eq(IsvChannelConfig::getIsvNo, isvNo)
                .list();
    }

    /**
     * 根据服务商号和通道查询
     */
    public Optional<IsvChannelConfig> findByIsvNoAndChannel(String isvNo, String channel) {
        return lambdaQuery()
                .eq(IsvChannelConfig::getChannel, channel)
                .eq(IsvChannelConfig::getIsvNo, isvNo)
                .oneOpt();
    }

    /**
     * 判断是服务商否存在指定的通道配置
     */
    public boolean existsByIsvNoAndChannel(String isvNo, String channel) {
        return lambdaQuery()
                .eq(IsvChannelConfig::getIsvNo, isvNo)
                .eq(IsvChannelConfig::getChannel, channel)
                .exists();
    }

}
