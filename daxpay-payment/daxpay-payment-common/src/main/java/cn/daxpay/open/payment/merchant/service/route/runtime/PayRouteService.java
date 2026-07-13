package cn.daxpay.open.payment.merchant.service.route.runtime;

import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.service.route.basic.PayRouteBasicMatcher;
import cn.daxpay.open.payment.merchant.service.route.scene.PayRouteSceneMatcher;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.merchant.service.route.model.RouteHit;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.service.route.model.PayRouteBundle;
import cn.daxpay.open.payment.core.strategy.ProductStrategySupport;
import cn.daxpay.open.payment.core.route.PayRouteFacade;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 支付通道路由服务
///
/// 实现 PayRouteFacade，供 NormalPayService 在支付流程中调用。
/// 两种解析路径：
/// 1. 直定模式：已传 channelMchNo，跳过路由，由 channelMchNo 推导产品；capability 必填，method 未传时由能力反推。
/// 2. 路由模式：按 appId 加载策略，经基础/场景模式匹配后回填 product/channelMchNo/capability。
/// 调用方需保证 appId 已解析完毕。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteService implements PayRouteFacade {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteSceneConfigManager sceneConfigManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final PayRouteProductResolver productResolver;
    private final PayRouteBasicMatcher basicMatcher;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

    /// 实付路由解析：直定模式优先，否则按策略模式匹配
    @Override
    public void resolve(NormalPayParam payParam) {
        // 直定模式：指定通道商户号，跳过路由
        if (StrUtil.isNotBlank(payParam.getChannelMchNo())) {
            resolveDirect(payParam);
            return;
        }
        // 路由模式: 支付方式必填(Bean Validation 已放宽以兼容直定可空, 此处显式校验)
        if (StrUtil.isBlank(payParam.getMethod())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.methodRequired");
        }
        String appId = payParam.getAppId();
        var bundle = loadBundle(appId);
        if (bundle == null || bundle.getStrategy() == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyNotFound");
        }
        PayRouteStrategy strategy = bundle.getStrategy();
        RouteHit hit = resolveByMode(bundle, payParam, strategy.getMode());
        if (hit == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.noMatch");
        }
        fillPayParam(payParam, hit);
    }

    /// 直定模式：capability 必填，由通道商户推导产品；method 未传时由(通道商户, 能力)反推回填
    private void resolveDirect(NormalPayParam payParam) {
        String channelMchNo = payParam.getChannelMchNo();
        String capability = payParam.getCapability();
        // 传值模式: 支付能力必填
        if (StrUtil.isBlank(capability)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityRequired");
        }
        String product = productResolver.productOfChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        String method = payParam.getMethod();
        if (StrUtil.isBlank(method)) {
            // 传值模式: 由(通道商户, 支付能力)反推支付方式, 供下游通道策略使用
            String inferred = payRouteStrategyCapabilitySupport.inferMethodForCapability(channelMchNo, capability);
            if (inferred == null) {
                // 支付能力[{0}]与通道商户[{1}]不匹配
                PayCapabilityEnum capEnum = PayCapabilityEnum.findByCode(capability);
                String capLabel = capEnum != null ? I18nUtil.getEnumName(capEnum) : capability;
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.directCapabilityChannelMchMismatch", capLabel, channelMchNo);
            }
            payParam.setMethod(inferred);
        } else {
            // 已传支付方式: 校验(产品, 方式, 能力)一致
            validateCapability(product, PayMethodEnum.findByCode(method), capability);
        }
        payParam.setProduct(product);
    }

    /// 按应用号从库加载路由数据包（无 Redis 缓存）
    private PayRouteBundle loadBundle(String appId) {
        var strategyOpt = strategyManager.findByAppId(appId);
        if (strategyOpt.isEmpty()) {
            return null;
        }
        var strategy = strategyOpt.get();
        return new PayRouteBundle()
                .setStrategy(strategy)
                .setBasicConfigs(basicConfigManager.findByStrategyId(strategy.getId()))
                .setSceneConfigs(sceneConfigManager.findByStrategyId(strategy.getId()));
    }

    /// 按路由模式委托匹配器；未识别模式按场景模式处理
    private RouteHit resolveByMode(PayRouteBundle bundle, NormalPayParam payParam, String mode) {
        if (Objects.equals(mode, PayRouteModeEnum.BASIC.getCode())) {
            return basicMatcher.match(bundle.getBasicConfigs(), payParam);
        }
        return PayRouteSceneMatcher.match(bundle.getSceneConfigs(), payParam);
    }

    /// 将命中结果写入 NormalPayParam：回填 product(由通道商户号派生)、通道商户号、支付能力
    private void fillPayParam(NormalPayParam payParam, RouteHit hit) {
        String product = StrUtil.isNotBlank(hit.product())
                ? hit.product()
                : productResolver.productOfChannelMchNo(hit.channelMchNo());
        payParam.setProduct(product);
        if (StrUtil.isNotBlank(hit.channelMchNo())) {
            payParam.setChannelMchNo(hit.channelMchNo());
        }
        if (StrUtil.isBlank(payParam.getCapability()) && StrUtil.isNotBlank(hit.capability())) {
            payParam.setCapability(hit.capability());
        }
    }

    /// 校验指定能力属于该(产品, 支付方式)的策略声明候选
    private void validateCapability(String product, PayMethodEnum method, String capability) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        if (!ProductStrategySupport.strategySupportsCapabilityCode(strategy, method.getCode(), capability)) {
            // 支付能力[{0}]与产品[{1}]、支付方式[{2}]不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityProductMismatch", capability, product, method.getCode());
        }
    }
}
