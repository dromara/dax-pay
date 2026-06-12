package org.dromara.daxpay.payment.merchant.dao.route.strategy;

import org.dromara.daxpay.payment.merchant.entity.route.strategy.PayRouteStrategy;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付通道路由策略 Mapper
///
@Mapper
public interface PayRouteStrategyMapper extends MPJBaseMapper<PayRouteStrategy> {
}
