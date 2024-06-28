package cn.daxpay.multi.service.common.cache;

import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.service.dao.channel.ChannelConfigManager;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final Cache<String, ChannelConfig> cache = CacheUtil.newLRUCache(100);

    private final ChannelConfigManager channelConfigManager;

    /**
     * 获取通道配置
     */
    public ChannelConfig get(String appId, String channel) {
        String key = appId + "_" + channel;
        ChannelConfig channelConfig = cache.get(key);
        if (channelConfig == null) {
            channelConfig = channelConfigManager.findByAppIdAndChannel(appId, channel)
                    .orElseThrow(() -> new ConfigNotEnableException("未找到指定的通道配置"));
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
