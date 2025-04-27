package org.dromara.daxpay.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 通道配置 constant
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelConfigManager extends BaseManager<ChannelConfigMapper, ChannelConfig> {

    /**
     * 根据id进行更新
     */
    @Override
    @CacheEvict(value = "cache:channelConfig", key = "#channelConfig.appId + ':' + #channelConfig.channel")
    public int updateById(ChannelConfig channelConfig) {
        return super.updateById(channelConfig);
    }

    /**
     * 批量更新
     */
    @Override
    @CacheEvict(value = "cache:channelConfig", allEntries = true)
    public boolean updateAllById(Collection<ChannelConfig> entityList) {
        return super.updateAllById(entityList);
    }

    /**
     * 根据应用号查询
     */
    public List<ChannelConfig> findByAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }
    /**
     * 根据应用号查询启用的配置
     */
    public List<ChannelConfig> findEnableByAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::isEnable, true)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }

    /**
     * 根据应用号和通道查询
     */
    public Optional<ChannelConfig> findByAppIdAndChannel(String appId, String channel) {
        return lambdaQuery()
                .eq(ChannelConfig::getAppId, appId)
                .eq(ChannelConfig::getChannel, channel)
                .oneOpt();
    }

    /**
     * 判断是应用否存在指定的通道配置
     */
    public boolean existsByAppIdAndChannel(String appId, String channel) {
        return lambdaQuery()
                .eq(ChannelConfig::getAppId, appId)
                .eq(ChannelConfig::getChannel, channel)
                .exists();
    }

}
