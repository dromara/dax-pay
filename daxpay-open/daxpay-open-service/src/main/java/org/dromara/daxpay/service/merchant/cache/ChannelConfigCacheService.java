package org.dromara.daxpay.service.merchant.cache;

import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.service.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 支付通道配置缓存服务
 * @author xxm
 * @since 2024/6/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConfigCacheService {

    private final ChannelConfigManager channelConfigManager;

    /**
     * 获取通道配置
     */
    @Cacheable(value = "cache:channelConfig", key = "#appId + ':' + #channel")
    public ChannelConfig getMchChannelConfig(String appId, String channel) {
        return channelConfigManager.findByAppIdAndChannel(appId, channel)
                .orElseThrow(() -> new ConfigNotEnableException("未找到指定的支付通道配置"));
    }

}
