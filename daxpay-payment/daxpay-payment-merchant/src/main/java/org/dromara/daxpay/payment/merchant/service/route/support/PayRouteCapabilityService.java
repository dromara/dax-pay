package org.dromara.daxpay.payment.merchant.service.route.support;

import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.masterdata.constants.product.service.PayProductCapabilityService;
import org.dromara.daxpay.payment.masterdata.constants.provider.service.PayProviderMethodService;
import org.dromara.daxpay.payment.strategy.product.AbsProductStrategy;
import org.dromara.daxpay.payment.old.pay.support.ProductStrategySupport;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// # 通道路由支付能力
///
/// 渠道目录 × 支付产品 × 策略能力 求交与校验。
@Service
@RequiredArgsConstructor
public class PayRouteCapabilityService {

    private final PayProviderMethodService payProviderMethodService;
    private final PayProductCapabilityService payProductCapabilityService;

    /// 目录与支付产品已挂载能力求交：指定支付产品在该支付渠道下可用的支付方式
    public List<String> methodsForProductPayProvider(String product, String providerCode) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null) {
            return List.of();
        }
        List<String> methodCodes = new ArrayList<>();
        for (PayMethodEnum directoryMethod : payProviderMethodService.listMethodsForBrand(provider)) {
            String methodCode = directoryMethod.getCode();
            if (productSupportsMethod(product, providerCode, methodCode)) {
                methodCodes.add(methodCode);
            }
        }
        return methodCodes;
    }

    /// 支付产品是否支持目录中的 provider + method 组合（策略方式→能力 Map ∧ `pay_product_capability`）
    public boolean productSupportsMethod(String product, String providerCode, String methodCode) {
        if (!payProviderMethodService.contains(providerCode, methodCode)) {
            return false;
        }
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return false;
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        if (!ProductStrategySupport.supportsDirectoryMethod(strategy, method)) {
            return false;
        }
        return payProductCapabilityService.productSupportsMethod(product, methodCode);
    }
}
