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

import static org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum.UNION_PAY;

/// # 银联商务B扫C支付（被扫）产品策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsBarcodeProductStrategy extends AbsProductStrategy {

    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            PayMethodEnum.UNION_PAY_BARCODE, List.of(PayCapabilityEnum.UNION_PAY_BARCODE));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_BARCODE;
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
        return List.of(UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
