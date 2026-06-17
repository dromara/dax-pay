package org.dromara.daxpay.channel.douyin.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelApiCallMode;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelPayIdType;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.payment.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.DOUYIN;

/// # 抖音支付直连产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinPayProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.DOUYIN_QR, List.of(PayCapabilityEnum.DOUYIN_QR),
            PayMethodEnum.DOUYIN_JSAPI, List.of(PayCapabilityEnum.DOUYIN_JSAPI),
            PayMethodEnum.DOUYIN_H5, List.of(PayCapabilityEnum.DOUYIN_H5),
            PayMethodEnum.DOUYIN_APP, List.of(PayCapabilityEnum.DOUYIN_APP));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public boolean isAllocatable() {
        return false;
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
        return List.of(DOUYIN);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
