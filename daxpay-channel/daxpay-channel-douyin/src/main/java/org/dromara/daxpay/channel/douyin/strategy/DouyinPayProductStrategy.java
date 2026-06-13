package org.dromara.daxpay.channel.douyin.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelApiCallMode;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelPayIdType;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/// # 抖音支付直连产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinPayProductStrategy extends AbsProductStrategy {

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public boolean isApply() {
        return true;
    }

    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.MCH;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return Collections.emptyList();
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return Collections.emptyMap();
    }
}
