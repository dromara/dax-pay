package org.dromara.daxpay.payment.merchant.service.route.basic;

import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.merchant.dao.config.MchProductConfigManager;
import org.dromara.daxpay.payment.merchant.entity.config.MchProductConfig;
import org.dromara.daxpay.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteCapabilityService;
import org.dromara.daxpay.payment.merchant.service.route.model.RouteHit;
import org.dromara.daxpay.payment.merchant.service.route.runtime.PayRouteProductResolver;
import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteI18nHelper;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/// # 基础模式通道路由匹配器
///
/// 按已配置的支付渠道与支付产品，结合产品策略解析通道与支付方式
@Component
@RequiredArgsConstructor
public class PayRouteBasicMatcher {

    private final MchProductConfigManager productConfigManager;
    private final PayRouteProductResolver productResolver;
    private final PayRouteCapabilityService payRouteCapabilityService;

    /// 基础模式匹配
    public RouteHit match(List<PayRouteBasicConfig> basicConfigs, PayParam payParam) {
        if (StrUtil.isBlank(payParam.getProvider())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequired");
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(payParam.getProvider());
        if (provider == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.basicProviderInvalid");
        }
        String product = findConfiguredProduct(basicConfigs, payParam.getProvider());
        if (StrUtil.isBlank(product)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotConfigured", PayRouteI18nHelper.provider(payParam.getProvider()));
        }
        assertProductConfigured(payParam.getMchNo(), product, provider);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        List<String> methodCandidates = payRouteCapabilityService.methodsForProductPayProvider(
                product, payParam.getProvider());
        if (methodCandidates.isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.methodNotSupportedForProduct",
                    PayRouteI18nHelper.product(product));
        }
        String method = methodCandidates.getFirst();
        String channel = productResolver.channelOfProduct(product);
        return new RouteHit(channel, method, product, null, null);
    }

    /// 从基础配置中取指定支付渠道已绑定的产品编码
    private String findConfiguredProduct(List<PayRouteBasicConfig> basicConfigs, String providerCode) {
        if (basicConfigs == null) {
            return null;
        }
        return basicConfigs.stream()
                .filter(config -> Objects.equals(config.getProvider(), providerCode))
                .map(PayRouteBasicConfig::getProduct)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    /// 路由配置的产品仍须在商户已开通产品范围内，且产品策略支持该支付渠道
    private void assertProductConfigured(String mchNo, String product, PayProviderEnum provider) {
        boolean mchEnabled = productConfigManager.findByMchNo(mchNo).stream()
                .filter(MchProductConfig::isEnable)
                .anyMatch(config -> Objects.equals(config.getProduct(), product));
        if (!mchEnabled) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", PayRouteI18nHelper.provider(provider.getCode()));
        }
        if (!PaymentStrategyFactory.productSupportsProvider(product, provider)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", PayRouteI18nHelper.provider(provider.getCode()));
        }
    }
}
