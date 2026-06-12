package org.dromara.daxpay.payment.merchant.service.route.runtime;

import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import org.dromara.daxpay.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import org.dromara.daxpay.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import org.dromara.daxpay.payment.merchant.service.route.basic.PayRouteBasicMatcher;
import org.dromara.daxpay.payment.merchant.service.route.scene.PayRouteSceneMatcher;
import org.dromara.daxpay.payment.merchant.service.route.model.RouteHit;
import org.dromara.daxpay.payment.merchant.entity.route.strategy.PayRouteStrategy;
import org.dromara.daxpay.payment.merchant.service.route.model.PayRouteBundle;
import org.dromara.daxpay.payment.pay.service.route.PayRouteFacade;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.enums.pay.route.PayRouteModeEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 支付通道路由服务
///
/// 实现 PayRouteFacade，供管理端试算按策略模式解析通道、方式与产品。
/// `resolve` 暂未接入支付切面（起步阶段仅配置态 + 试算）；实付接入时再打开 PaymentVerifyAspect 调用。
/// 当前仅实现基础模式与场景模式；精细模式 advanced 暂未开放。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteService implements PayRouteFacade {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteSceneConfigManager sceneConfigManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final PayRouteProductResolver productResolver;
    private final PayRouteBasicMatcher basicMatcher;
    private final PaymentContext paymentContext;

    /// 实付路由解析：已指定 product 则跳过；否则按策略模式匹配并回填 PayParam（暂未接入支付切面）
    @Override
    public void resolve(PayParam payParam) {
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            return;
        }
        String appId = payParam.getAppId();
        if (StrUtil.isBlank(appId)) {
            appId = paymentContext.getTradeInfo().getAppId();
            payParam.setAppId(appId);
        }
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

    /// 模拟路由解析（modeOverride 为空时使用策略生效模式）
    public RouteHit simulate(PayParam payParam, String modeOverride) {
        var bundle = loadBundle(payParam.getAppId());
        if (bundle == null || bundle.getStrategy() == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyNotFound");
        }
        PayRouteStrategy strategy = bundle.getStrategy();
        String mode = StrUtil.isNotBlank(modeOverride) ? modeOverride : strategy.getMode();
        RouteHit hit = resolveByMode(bundle, payParam, mode);
        if (hit == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.noMatch");
        }
        fillPayParam(payParam, hit);
        return hit;
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

    /// 按路由模式委托匹配器；advanced 拒绝；未识别模式按场景模式处理
    private RouteHit resolveByMode(PayRouteBundle bundle, PayParam payParam, String mode) {
        if (Objects.equals(mode, PayRouteModeEnum.ADVANCED.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.advancedModeNotSupported");
        }
        if (Objects.equals(mode, PayRouteModeEnum.BASIC.getCode())) {
            return basicMatcher.match(bundle.getBasicConfigs(), payParam);
        }
        return PayRouteSceneMatcher.match(bundle.getSceneConfigs(), payParam);
    }

    /// 将命中结果写入 PayParam；产品为空时按通道+方式反查商户产品
    private void fillPayParam(PayParam payParam, RouteHit hit) {
        String product = StrUtil.isNotBlank(hit.product())
                ? hit.product()
                : productResolver.resolve(payParam.getMchNo(), hit.channel(), hit.method());
        payParam.setChannel(hit.channel());
        payParam.setMethod(hit.method());
        payParam.setProduct(product);
    }
}
