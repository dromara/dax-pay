package org.dromara.daxpay.service.service.config;

import org.dromara.daxpay.service.common.cache.MchAppCacheService;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.dao.constant.ChannelConstManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.entity.constant.ChannelConst;
import org.dromara.daxpay.service.entity.merchant.MchApp;
import org.dromara.daxpay.service.result.config.ChannelConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConfigService {

    private final ChannelConfigManager channelConfigManager;

    private final ChannelConstManager channelConstManager;

    private final MchAppCacheService mchAppCacheService;

    /**
     * 通道配置列表, 根据应用进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
     */
    public List<ChannelConfigResult> findAllByAppId(String appId){
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), (v1, v2) -> v1));
        // 遍历通道类型
        List<ChannelConst> channelList = channelConstManager.findAllByEnable();
        MchApp mchApp = mchAppCacheService.get(appId);
        return channelList.stream().map(o->{
            ChannelConfig channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new ChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName())
                        .setAppId(mchApp.getAppId());
            } else {
                return channelConfig.toResult()
                        .setName(o.getName())
                        .setAppId(mchApp.getAppId());
            }
        }).toList();
    }
}
