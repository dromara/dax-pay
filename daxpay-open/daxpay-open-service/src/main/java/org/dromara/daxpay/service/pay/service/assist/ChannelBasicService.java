package org.dromara.daxpay.service.pay.service.assist;

import cn.bootx.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.service.pay.strategy.AbsChannelBasicStrategy;
import org.dromara.daxpay.service.pay.util.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道基础数据获取
 * @author xxm
 * @since 2025/6/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelBasicService {


    /**
     * 获取支付方式
     */
    public List<LabelValue> payMethodList(String channel){
        var basicStrategy = PaymentStrategyFactory.create(channel, AbsChannelBasicStrategy.class);
        return basicStrategy.payMethodList().stream()
                .map(keyValue -> new LabelValue(keyValue.getValue(), keyValue.getKey()))
                .toList();

    }
}
