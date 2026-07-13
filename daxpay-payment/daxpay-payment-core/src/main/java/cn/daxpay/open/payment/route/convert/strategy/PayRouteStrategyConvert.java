package cn.daxpay.open.payment.route.convert.strategy;

import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.param.strategy.PayRouteStrategyParam;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由策略转换
///
@Mapper
public interface PayRouteStrategyConvert {

    PayRouteStrategyConvert CONVERT = Mappers.getMapper(PayRouteStrategyConvert.class);

    PayRouteStrategyResult toResult(PayRouteStrategy entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PayRouteStrategyParam param, @MappingTarget PayRouteStrategy entity);
}
