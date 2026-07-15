package cn.daxpay.open.payment.route.convert.basic;

import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.result.basic.PayRouteBasicConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由基础模式配置转换
///
/// 同名映射；channelMchants(可选通道商户列表)由 service 按商户开通情况装配
@Mapper
public interface PayRouteBasicConfigConvert {

    PayRouteBasicConfigConvert CONVERT = Mappers.getMapper(PayRouteBasicConfigConvert.class);

    PayRouteBasicConfigResult toResult(PayRouteBasicConfig entity);
}
