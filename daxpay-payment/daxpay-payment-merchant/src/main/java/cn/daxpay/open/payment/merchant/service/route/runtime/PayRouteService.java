package cn.daxpay.open.payment.merchant.service.route.runtime;

import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.service.route.basic.PayRouteBasicMatcher;
import cn.daxpay.open.payment.merchant.service.route.scene.PayRouteSceneMatcher;
import cn.daxpay.open.payment.merchant.service.route.model.RouteHit;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.service.route.model.PayRouteBundle;
import cn.daxpay.open.payment.old.pay.support.ProductStrategySupport;
import cn.daxpay.open.payment.pay.service.route.PayRouteFacade;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
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
/// 三种解析路径：
/// 1. 直定模式：已传 channelMchNo，跳过路由，直接由 channelMchNo 推导产品并校验/派生能力；
/// 2. 兼容跳过：未传 channelMchNo 但已指定 product(重付复用)，直接返回；
/// 3. 路由模式：按 appId 加载策略，经基础/场景模式匹配后回填 product/channelMchNo/capability。
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

    /// 实付路由解析：直定模式优先，其次兼容已指定 product，最后按策略模式匹配
    @Override
    public void resolve(PayParam payParam) {
        // 直定模式：指定通道商户号，跳过路由
        if (StrUtil.isNotBlank(payParam.getChannelMchNo())) {
            resolveDirect(payParam);
            return;
        }
        // 兼容：已指定 product(重付复用)，跳过路由
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            return;
        }
        String appId = payParam.getAppId();
        var bundle = loadBundle(appId);
        if (bundle == null || bundle.getStrategy() == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyNotFound");
        }
        PayRouteStrategy strategy = bundle.getStrategy();
        if (!strategy.isEnable()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyDisabled");
        }
        RouteHit hit = resolveByMode(bundle, payParam, strategy.getMode());
        if (hit == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.noMatch");
        }
        fillPayParam(payParam, hit);
    }

    /// 直定模式：由通道商户号推导产品，校验或派生支付能力
    private void resolveDirect(PayParam payParam) {
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 场景模式下须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        String channelMchNo = payParam.getChannelMchNo();
        String product = productResolver.productOfChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        String capability = payParam.getCapability();
        if (StrUtil.isBlank(capability)) {
            // 未传能力则按 (产品, 支付方式) 派生
            capability = inferCapability(product, methodEnum);
        } else {
            // 已传能力则校验属于该(产品, 支付方式)候选
            validateCapability(product, methodEnum, capability);
        }
        payParam.setProduct(product);
        payParam.setCapability(capability);
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
    private RouteHit resolveByMode(PayRouteBundle bundle, PayParam payParam, String mode) {
        if (Objects.equals(mode, "basic")) {
            return basicMatcher.match(bundle.getBasicConfigs(), payParam);
        }
        return PayRouteSceneMatcher.match(bundle.getSceneConfigs(), payParam);
    }

    /// 将命中结果写入 PayParam：回填 product(由通道商户号派生)、通道商户号、支付能力
    private void fillPayParam(PayParam payParam, RouteHit hit) {
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

    /// 派生支付能力：取产品策略声明的(方式→能力)首个
    private String inferCapability(String product, PayMethodEnum method) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        List<PayCapabilityEnum> capabilities = ProductStrategySupport.capabilitiesForMethod(strategy, method);
        return capabilities.isEmpty() ? null : capabilities.getFirst().getCode();
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
