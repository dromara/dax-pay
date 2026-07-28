package cn.daxpay.open.payment.route.service.runtime;

import cn.daxpay.open.payment.route.dao.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.route.dao.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.service.model.PayRouteBundle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/// # 通道路由数据包缓存服务
///
/// 从 [PayRouteService] 抽出，使 @Cacheable 能正常生效（原方法为 private + 自调用，AOP 代理不覆盖）。
/// 缓存 key 为 appId，缓存名 `payment:route-bundle`，写侧通过 @CacheEvict 失效。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteBundleService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final PayRouteSceneConfigManager sceneConfigManager;

    /// 按应用号加载路由数据包（命中缓存时不查库）
    @Cacheable(value = "payment:route-bundle", key = "#appId")
    public PayRouteBundle loadBundle(String appId) {
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
}