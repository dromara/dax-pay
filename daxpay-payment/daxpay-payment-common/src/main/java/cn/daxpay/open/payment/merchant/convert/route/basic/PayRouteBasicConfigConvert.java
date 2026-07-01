package cn.daxpay.open.payment.merchant.convert.route.basic;

import cn.daxpay.open.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由基础模式配置转换
///
@Mapper
public interface PayRouteBasicConfigConvert {

    PayRouteBasicConfigConvert CONVERT = Mappers.getMapper(PayRouteBasicConfigConvert.class);

    /// channelMchants(可选通道商户列表)由 service 按商户开通情况装配，转换时忽略
    @Mapping(target = "channelMchants", ignore = true)
    PayRouteBasicConfigResult toResult(PayRouteBasicConfig entity);
}
