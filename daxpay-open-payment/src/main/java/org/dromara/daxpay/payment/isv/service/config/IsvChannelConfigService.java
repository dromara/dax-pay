package org.dromara.daxpay.payment.isv.service.config;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.isv.result.config.IsvChannelConfigResult;
import org.dromara.daxpay.payment.pay.dao.constant.ChannelConstManager;
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

    private final IsvInfoManager isvInfoManager;

    /**
     * 通道配置列表, 根据服务商进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
     */
    public List<IsvChannelConfigResult> findAllByIsvNo(String isvNo){
        Map<String, IsvChannelConfig> channelConfigMap = channelConfigManager.findByAppId(isvNo)
                .stream()
                .collect(Collectors.toMap(IsvChannelConfig::getChannel, Function.identity(), CollectorsFunction::retainFirst));
        // 遍历通道类型
        var channelList = channelConstManager.findAllByIsvAndEnable();
        IsvInfo isvInfo = isvInfoManager.findByIsvNo(isvNo)
                .orElseThrow(() -> new DataNotExistException("服务商不存在"));
        return channelList.stream().map(o->{
            var channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new IsvChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName())
                        .setIsvNo(isvInfo.getIsvNo());
            } else {
                return channelConfig.toResult()
                        .setName(o.getName())
                        .setIsvNo(isvInfo.getIsvNo());
            }
        }).toList();
    }


}
