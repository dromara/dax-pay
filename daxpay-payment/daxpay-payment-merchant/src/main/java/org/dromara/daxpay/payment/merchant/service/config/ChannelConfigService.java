package org.dromara.daxpay.payment.merchant.service.config;

import org.dromara.daxpay.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.pay.service.masterdata.channel.PayChannelMasterDataService;
import org.dromara.daxpay.payment.pay.entity.config.ChannelConfig;
import org.dromara.daxpay.payment.pay.result.masterdata.channel.PayChannelResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 通道配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConfigService {

    private final ChannelConfigManager channelConfigManager;

    private final PayChannelMasterDataService PayChannelMasterDataService;

    private final PaymentAssistService paymentAssistService;

    private final PaymentContext apiContext;

    /// 通道配置列表, 根据应用进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
    public List<ChannelConfigResult> findAllByAppId(String appId){
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), CollectorsFunction::retainFirst));
        // 商户应用
        paymentAssistService.initMchAndApp(appId);
        // 遍历通道类型,
        var channelList = PayChannelMasterDataService.listAll();
        return channelList.stream()
                .map(o->{
            ChannelConfig channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new ChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName())
                        .setMchNo(apiContext.getTradeInfo().getMchNo())
                        .setAppId(apiContext.getTradeInfo().getAppId());
            } else {
                return new ChannelConfigResult()
                        .setId(channelConfig.getId())
                        .setChannel(channelConfig.getChannel())
                        .setName(o.getName())
                        .setEnable(channelConfig.isEnable())
                        .setMchNo(apiContext.getTradeInfo().getMchNo())
                        .setAppId(apiContext.getTradeInfo().getAppId());
            }
        }).toList();
    }

    /// 启用的通道下拉列表
    public List<LabelValue> dropdownByEnable(String appId) {
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), CollectorsFunction::retainFirst));

        // 商户应用
        paymentAssistService.initMchAndApp(appId);
        // 遍历通道类型
        List<PayChannelResult> channelList = PayChannelMasterDataService.listAll();
        var constMap = channelList.stream().collect(Collectors.toMap(PayChannelResult::getCode, Function.identity()));
        return channelList.stream()
                .map(o-> channelConfigMap.get(o.getCode()))
                .filter(Objects::nonNull)
                .filter(ChannelConfig::isEnable)
                .map(o-> new LabelValue()
                        .setLabel(constMap.get(o.getChannel()).getName())
                        .setValue(o.getChannel()))
                .toList();

    }

}
