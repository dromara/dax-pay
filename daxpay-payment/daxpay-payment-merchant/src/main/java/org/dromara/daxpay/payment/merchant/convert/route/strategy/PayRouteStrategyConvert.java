package org.dromara.daxpay.payment.merchant.convert.route.strategy;

import org.dromara.daxpay.payment.merchant.entity.route.strategy.PayRouteStrategy;
import org.dromara.daxpay.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由策略转换
///
@Mapper
public interface PayRouteStrategyConvert {

    PayRouteStrategyConvert CONVERT = Mappers.getMapper(PayRouteStrategyConvert.class);

    PayRouteStrategyResult toResult(PayRouteStrategy entity);
}
