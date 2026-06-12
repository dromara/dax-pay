package org.dromara.daxpay.payment.merchant.convert.route.basic;

import org.dromara.daxpay.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import org.dromara.daxpay.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由基础模式配置转换
///
@Mapper
public interface PayRouteBasicConfigConvert {

    PayRouteBasicConfigConvert CONVERT = Mappers.getMapper(PayRouteBasicConfigConvert.class);

    PayRouteBasicConfigResult toResult(PayRouteBasicConfig entity);
}
