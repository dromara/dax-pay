package org.dromara.daxpay.payment.pay.service.assist;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import org.dromara.daxpay.payment.pay.support.ProductStrategySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 通道基础数据获取
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelBasicService {

    /// 获取通道产品支持的支付能力列表
    public List<LabelValue> payMethodList(String channel) {
        var productStrategy = PaymentStrategyFactory.create(channel, AbsProductStrategy.class);
        return ProductStrategySupport.supportedPayCapabilities(productStrategy).stream()
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

}
