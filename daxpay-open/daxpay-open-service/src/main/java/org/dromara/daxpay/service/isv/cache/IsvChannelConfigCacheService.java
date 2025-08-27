package org.dromara.daxpay.service.isv.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.service.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.service.isv.entity.config.IsvChannelConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 服务商通道配置缓存服务
 * @author xxm
 * @since 2024/10/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvChannelConfigCacheService {

    private final IsvChannelConfigManager channelConfigManager;

    /**
     * 获取服务商通道配置
     */
    @Cacheable(value = "cache:isv:channelConfig", key = "#channel")
    public IsvChannelConfig get(String channel) {
        return channelConfigManager.findByChannel(channel)
                .orElseThrow(() -> new ConfigNotEnableException("未找到指定的服务商通道配置"));
    }

}
