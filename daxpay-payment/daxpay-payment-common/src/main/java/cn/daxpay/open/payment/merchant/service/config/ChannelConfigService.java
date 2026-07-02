package cn.daxpay.open.payment.merchant.service.config;

import cn.daxpay.open.platform.common.mybatisplus.function.CollectorsFunction;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.common.runtime.PaymentContext;
import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.merchant.dao.config.ChannelConfigManager;
import cn.daxpay.open.payment.merchant.result.config.ChannelConfigResult;
import cn.daxpay.open.payment.masterdata.constants.channel.service.PayChannelService;
import cn.daxpay.open.payment.masterdata.config.entity.ChannelConfig;
import cn.daxpay.open.payment.masterdata.constants.channel.result.PayChannelResult;
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

    private final PayChannelService payChannelService;

    private final MerchantContextLoader merchantContextLoader;

    private final PaymentContext paymentContext;

    /// 通道配置列表, 根据应用进行查询, 默认返回所有通道配置, 如果未进行配置启用状态会为null
    public List<ChannelConfigResult> findAllByAppId(String appId){
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), CollectorsFunction::retainFirst));
        // 按应用反推并初始化商户身份(mchNo 进入上下文)
        merchantContextLoader.initMchByApp(appId);
        String mchNo = paymentContext.getMchNo();
        // 遍历通道类型,
        var channelList = payChannelService.listAll();
        return channelList.stream()
                .map(o->{
            ChannelConfig channelConfig = channelConfigMap.get(o.getCode());
            if (Objects.isNull(channelConfig)){
                return new ChannelConfigResult()
                        .setChannel(o.getCode())
                        .setName(o.getName())
                        .setMchNo(mchNo)
                        .setAppId(appId);
            } else {
                return new ChannelConfigResult()
                        .setId(channelConfig.getId())
                        .setChannel(channelConfig.getChannel())
                        .setName(o.getName())
                        .setEnable(channelConfig.isEnable())
                        .setMchNo(mchNo)
                        .setAppId(appId);
            }
        }).toList();
    }

    /// 启用的通道下拉列表
    public List<LabelValue> dropdownByEnable(String appId) {
        Map<String, ChannelConfig> channelConfigMap = channelConfigManager.findByAppId(appId)
                .stream()
                .collect(Collectors.toMap(ChannelConfig::getChannel, Function.identity(), CollectorsFunction::retainFirst));

        // 按应用反推并初始化商户身份
        merchantContextLoader.initMchByApp(appId);
        // 遍历通道类型
        List<PayChannelResult> channelList = payChannelService.listAll();
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
