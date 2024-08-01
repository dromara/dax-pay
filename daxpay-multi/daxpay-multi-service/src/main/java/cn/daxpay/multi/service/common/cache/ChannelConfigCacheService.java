package cn.daxpay.multi.service.common.cache;

import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 通道配置缓存服务
 * @author xxm
 * @since 2024/6/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConfigCacheService {
    private final Cache<String, ChannelConfig> cache = CacheUtil.newLRUCache(1024,15 * 60 * 1000);

    private final ChannelConfigManager channelConfigManager;

    /**
     * 获取通道配置
     */
    @Cacheable(value = "cache:channelConfig", key = "#appId + ':' + #channel")
    public ChannelConfig get(String appId, String channel) {
        String key = appId + "_" + channel;
        ChannelConfig channelConfig = cache.get(key);
        if (channelConfig == null) {
            channelConfig = channelConfigManager.findByAppIdAndChannel(appId, channel)
                    .orElseThrow(() -> new ConfigNotEnableException("未找到指定的支付通道配置"));
//            cache.put(key, channelConfig);
        }
        return channelConfig;
    }

    /**
     * 清楚并更新通道配置
     */
    public void remove(String appId, String channel) {
        String key = appId + "_" + channel;
        cache.remove(key);
    }

}
