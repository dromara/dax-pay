package org.dromara.daxpay.service.isv.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.service.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.service.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.service.isv.result.config.IsvChannelConfigResult;
import org.dromara.daxpay.service.pay.dao.constant.ChannelConstManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 服务商通道配置
 * @author xxm
 * @since 2024/10/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvChannelConfigService {

    private final IsvChannelConfigManager channelConfigManager;

    private final ChannelConstManager channelConstManager;

    /**
     * 通道配置列表, 根据服务商进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
     */
    public List<IsvChannelConfigResult> findAll(){
        Map<String, IsvChannelConfig> channelConfigMap = channelConfigManager.findAll()
                .stream()
                .collect(Collectors.toMap(IsvChannelConfig::getChannel, Function.identity(), (v1, v2) -> v1));
        // 遍历通道类型
        var channelList = channelConstManager.findAllByIsvAndEnable();
        return channelList.stream().map(o->{
            var channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new IsvChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName());
            } else {
                return channelConfig.toResult()
                        .setName(o.getName());
            }
        }).toList();
    }


}
