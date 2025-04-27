package org.dromara.daxpay.service.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.MerchantTypeEnum;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.entity.constant.ChannelConst;
import org.dromara.daxpay.service.result.config.ChannelConfigResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
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

    private final org.dromara.daxpay.service.pay.dao.constant.ChannelConstManager channelConstManager;

    private final PaymentAssistService paymentAssistService;

    /**
     * 通道配置列表, 根据应用进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
     * 普通商户显示所有的配置
     * 特约商户显示服务商类型的配置
     */
    public List<ChannelConfigResult> findAllByAppId(String appId){
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), (v1, v2) -> v1));
        // 商户应用
        paymentAssistService.initMchAndApp(appId);
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        // 遍历通道类型, 特约商户不显示普通类型
        List<ChannelConst> channelList;
        if (Objects.equals(mchApp.getMchType(), MerchantTypeEnum.PARTNER.getCode())){
            // 特约商户只显示服务商类型通道
            channelList = channelConstManager.findAllByIsvAndEnable();
        } else {
            // 普通商户显示全部类型通道
            channelList = channelConstManager.findAllByEnable();
        }
        return channelList.stream().map(o->{
            ChannelConfig channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new ChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName())
                        .setMchNo(mchApp.getMchNo())
                        .setAppId(mchApp.getAppId());
            } else {
                return channelConfig.toResult()
                        .setName(o.getName())
                        .setMchNo(mchApp.getMchNo())
                        .setAppId(mchApp.getAppId());
            }
        }).toList();
    }

    /**
     * 启用的通道下拉列表
     */
    public List<LabelValue> dropdownByEnable(String appId) {
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), (v1, v2) -> v1));

        // 商户应用
        paymentAssistService.initMchAndApp(appId);
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        // 遍历通道类型, 特约商户不显示普通类型
        List<ChannelConst> channelList;
        if (Objects.equals(mchApp.getMchType(), MerchantTypeEnum.PARTNER.getCode())){
            // 特约商户只显示服务商类型通道
            channelList = channelConstManager.findAllByIsvAndEnable();
        } else {
            // 普通商户显示全部类型通道
            channelList = channelConstManager.findAllByEnable();
        }
        var constMap = channelList.stream().collect(Collectors.toMap(ChannelConst::getCode, Function.identity()));
        return channelList.stream()
                .map(o-> channelConfigMap.get(o.getCode()))
                .filter(Objects::nonNull)
                .filter(ChannelConfig::isEnable)
                .map(o-> new LabelValue()
                        .setLabel(constMap.get(o.getChannel()).getName())
                        .setValue(o.getChannel()))
                .toList();

    }

    /**
     * 判断通道配置是否启用
     */
    public boolean checkEnable(String appId, String channel){
        ChannelConfig channelConfig = channelConfigManager.findByAppIdAndChannel(appId, channel)
                .orElseThrow(() -> new DataNotExistException("通道配置不存在"));

        return channelConfig.isEnable();
    }
}
