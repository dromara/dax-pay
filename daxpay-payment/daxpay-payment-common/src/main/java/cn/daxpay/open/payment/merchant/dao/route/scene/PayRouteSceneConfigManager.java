package cn.daxpay.open.payment.merchant.dao.route.scene;

import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 支付通道路由场景模式配置 Manager
///
@Repository
public class PayRouteSceneConfigManager extends BaseManager<PayRouteSceneConfigMapper, PayRouteSceneConfig> {

    /// 按策略 ID 查询场景模式配置列表
    public List<PayRouteSceneConfig> findByStrategyId(Long strategyId) {
        return lambdaQuery()
                .eq(PayRouteSceneConfig::getStrategyId, strategyId)
                .list();
    }

    /// 按策略 ID 删除全部场景配置
    public void deleteByStrategyId(Long strategyId) {
        lambdaUpdate()
                .eq(PayRouteSceneConfig::getStrategyId, strategyId)
                .remove();
    }
}
