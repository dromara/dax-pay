package cn.daxpay.open.payment.merchant.service.route.basic;

import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.merchant.service.route.model.RouteHit;
import cn.daxpay.open.payment.merchant.service.route.runtime.PayRouteProductResolver;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.old.pay.support.ProductStrategySupport;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/// # 基础模式通道路由匹配器
///
/// 按「支付渠道 → 通道商户」配置，结合支付方式解析通道商户、产品与能力。
/// 支付渠道由支付方式(PayMethodEnum)自带渠道属性解析，不再从支付产品反推。
@Component
@RequiredArgsConstructor
public class PayRouteBasicMatcher {

    private final PayRouteProductResolver productResolver;

    /// 基础模式匹配
    public RouteHit match(List<PayRouteBasicConfig> basicConfigs, NormalPayParam payParam) {
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 场景模式下须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        // 支付方式自带渠道属性(OTHER 等无归属时报错)
        PayProviderEnum provider = methodEnum.getProvider();
        if (provider == null) {
            // 未指定支付产品时，支付渠道不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequired");
        }
        String channelMchNo = findConfiguredChannelMchNo(basicConfigs, provider.getCode());
        if (StrUtil.isBlank(channelMchNo)) {
            // 支付渠道[{0}]未配置通道商户，请在通道路由基础模式中完成配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicChannelMchNotConfigured", PayRouteI18nHelper.provider(provider.getCode()));
        }
        String product = productResolver.productOfChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        if (!PaymentStrategyFactory.productSupportsProvider(product, provider)) {
            // 支付渠道[{0}]下无可用支付产品
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", PayRouteI18nHelper.provider(provider.getCode()));
        }
        // 基础模式不存能力，由 (产品, 支付方式) 派生
        String capability = resolveCapability(product, methodEnum);
        return new RouteHit(product, channelMchNo, capability);
    }

    /// 从基础配置中取指定支付渠道已绑定的通道商户号
    private String findConfiguredChannelMchNo(List<PayRouteBasicConfig> basicConfigs, String providerCode) {
        if (basicConfigs == null) {
            return null;
        }
        return basicConfigs.stream()
                .filter(config -> Objects.equals(config.getProvider(), providerCode))
                .map(PayRouteBasicConfig::getChannelMchNo)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    /// 基础模式支付能力由产品策略声明的(方式→能力)映射取首个
    private String resolveCapability(String product, PayMethodEnum method) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        List<PayCapabilityEnum> capabilities = ProductStrategySupport.capabilitiesForMethod(strategy, method);
        return capabilities.isEmpty() ? null : capabilities.getFirst().getCode();
    }
}
