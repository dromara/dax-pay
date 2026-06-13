package org.dromara.daxpay.channel.ums.strategy;

import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.ALIPAY;
import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.WECHAT;

/// # 银联商务小程序支付产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsMiniProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI),
            PayMethodEnum.ALIPAY_MINI, List.of(PayCapabilityEnum.ALIPAY_MINI));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_MINI;
    }

    @Override
    public boolean isAllocatable() { return true; }

    @Override
    public boolean isTerminal() { return true; }

    @Override
    public boolean isApply() { return true; }

    @Override
    public boolean isSandbox() { return true; }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(WECHAT, ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
