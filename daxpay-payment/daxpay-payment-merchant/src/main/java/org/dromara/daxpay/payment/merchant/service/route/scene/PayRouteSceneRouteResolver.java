package org.dromara.daxpay.payment.merchant.service.route.scene;

import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteCapabilityService;
import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteI18nHelper;
import org.dromara.daxpay.payment.merchant.service.route.runtime.PayRouteProductResolver;
import org.dromara.daxpay.payment.strategy.product.AbsProductStrategy;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 场景模式路由解析（支付产品 + 支付方式 → 通道）
@Service
@RequiredArgsConstructor
public class PayRouteSceneRouteResolver {

    private final PayRouteProductResolver productResolver;
    private final PayRouteCapabilityService payRouteCapabilityService;
    private final PayRouteMethodValidator payRouteMethodValidator;

    /// 按产品、支付渠道与所选支付方式解析通道
    public SceneRoute resolve(String product, String providerCode, String method) {
        if (StrUtil.isBlank(product)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneProductRequired");
        }
        if (StrUtil.isBlank(method)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.basicProviderInvalid");
        }
        ProductEnum.findByCode(product);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        List<String> methodCandidates = payRouteCapabilityService.methodsForProductPayProvider(product, providerCode);
        if (methodCandidates.isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.methodNotSupportedForProduct",
                    PayRouteI18nHelper.product(product));
        }
        if (!methodCandidates.contains(method)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneMethodProductMismatch",
                    PayRouteI18nHelper.payMethod(method), PayRouteI18nHelper.product(product));
        }
        payRouteMethodValidator.validateSceneConfigItem(providerCode, method);
        String channel = productResolver.channelOfProduct(product);
        return new SceneRoute(channel, method, product);
    }

    /// 场景模式解析结果：通道、方式、产品
    public record SceneRoute(String channel, String method, String product) {
    }
}