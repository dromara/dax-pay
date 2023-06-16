package cn.bootx.platform.daxpay.core.merchant.service;

import cn.bootx.platform.daxpay.core.channel.config.dao.PayChannelConfigManager;
import cn.bootx.platform.daxpay.core.channel.config.entity.PayChannelConfig;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppPayConfigManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户应用支付配置
 *
 * @author xxm
 * @date 2023-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppPayConfigService {

    private final MchAppPayConfigManager mchAppPayConfigManager;

    private final PayChannelConfigManager channelConfigManager;

    /**
     * 根据应用ID删除
     */
    public void deleteByAppId(Long appId) {
        mchAppPayConfigManager.deleteByField(MchAppPayConfig::getAppCode, appId);
    }

    /**
     * 支付通道配置列表
     */
    public List<MchAppPayConfigResult> ListByAppId(String appCode) {
        // 首先查询系统中配置的支付通道进行排序
        List<PayChannelConfig> channels = channelConfigManager.findAllByOrder();
        // 查询当前应用所拥有的配置, 进行合并生成相关信息

        val mchAppPayConfigMap = mchAppPayConfigManager.findAllByField(MchAppPayConfig::getAppCode, appCode)
            .stream()
            .collect(Collectors.toMap(MchAppPayConfig::getChannel, Function.identity()));
        // 进行排序并返回
        return channels.stream().map(channel -> {
            MchAppPayConfig config = mchAppPayConfigMap.get(channel.getCode());
            MchAppPayConfigResult result = new MchAppPayConfigResult().setImg(channel.getImage())
                .setChannelCode(channel.getCode())
                .setChannelName(channel.getName());
            if (Objects.nonNull(config)) {
                result.setConfigId(config.getConfigId()).setState(config.getState());
            }
            return result;
        }).collect(Collectors.toList());
    }

}
