package cn.daxpay.open.payment.merchant.dao.route.strategy;

import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 支付通道路由策略 Manager
///
@Repository
public class PayRouteStrategyManager extends BaseManager<PayRouteStrategyMapper, PayRouteStrategy> {

    /// 按应用号查询路由策略
    public Optional<PayRouteStrategy> findByAppId(String appId) {
        return lambdaQuery()
                .eq(PayRouteStrategy::getAppId, appId)
                .oneOpt();
    }
}
