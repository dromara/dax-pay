package org.dromara.daxpay.service.isv.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.isv.entity.config.IsvChannelConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
     * 根据id进行更新
     */
    @Override
    @CacheEvict(value = "cache:isv:channelConfig", key = "#channelConfig.channel")
    public int updateById(IsvChannelConfig channelConfig) {
        return super.updateById(channelConfig);
    }

    /**
     * 批量更新
     */
    @Override
    @CacheEvict(value = "cache:isv:channelConfig", allEntries = true)
    public boolean updateAllById(Collection<IsvChannelConfig> entityList) {
        return super.updateAllById(entityList);
    }


    /**
     * 根据服务商号查询启用的配置
     */
    public List<IsvChannelConfig> findEnable() {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(IsvChannelConfig::isEnable, true)
                .list();
    }

    /**
     * 根据服务商号和通道查询
     */
    public Optional<IsvChannelConfig> findByChannel(String channel) {
        return lambdaQuery()
                .eq(IsvChannelConfig::getChannel, channel)
                .oneOpt();
    }

    /**
     * 判断是服务商否存在指定的通道配置
     */
    public boolean existsByChannel(String channel) {
        return lambdaQuery()
                .eq(IsvChannelConfig::getChannel, channel)
                .exists();
    }

}
