package cn.daxpay.open.payment.route.dao.basic;

import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 支付通道路由基础模式配置 Manager
///
@Repository
public class PayRouteBasicConfigManager extends BaseManager<PayRouteBasicConfigMapper, PayRouteBasicConfig> {

    /// 按策略 ID 查询基础模式配置列表
    public List<PayRouteBasicConfig> findByStrategyId(Long strategyId) {
        return lambdaQuery()
                .eq(PayRouteBasicConfig::getStrategyId, strategyId)
                .list();
    }

    /// 按策略 ID 删除全部基础配置
    public void deleteByStrategyId(Long strategyId) {
        lambdaUpdate()
                .eq(PayRouteBasicConfig::getStrategyId, strategyId)
                .remove();
    }
}
