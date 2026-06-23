package cn.daxpay.open.payment.merchant.service.route.runtime;

import cn.daxpay.open.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.service.route.basic.PayRouteBasicMatcher;
import cn.daxpay.open.payment.merchant.service.route.scene.PayRouteSceneMatcher;
import cn.daxpay.open.payment.merchant.service.route.model.RouteHit;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.service.route.model.PayRouteBundle;
import cn.daxpay.open.payment.pay.service.route.PayRouteFacade;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付通道路由服务
///
/// 实现 PayRouteFacade，供 NormalPayService 在支付流程中调用。
/// 已指定 product 则跳过；否则按 appId 加载策略，经基础/场景模式匹配后回填 product。
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

    /// 实付路由解析：已指定 product 则跳过；否则按策略模式匹配并回填 product
    @Override
    public void resolve(PayParam payParam) {
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

    /// 将命中结果写入 PayParam：仅回填 product（channel 从 ProductEnum 派生，method 由客户端传入）
    private void fillPayParam(PayParam payParam, RouteHit hit) {
        String product = StrUtil.isNotBlank(hit.product())
                ? hit.product()
                : productResolver.resolve(payParam.getMchNo(), hit.channel(), hit.method());
        payParam.setProduct(product);
    }
}
