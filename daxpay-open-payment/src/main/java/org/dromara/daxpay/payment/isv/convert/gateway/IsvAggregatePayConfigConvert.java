package org.dromara.daxpay.payment.isv.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregateBarPayConfig;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregatePayConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvAggregateBarPayConfigResult;
import org.dromara.daxpay.payment.isv.result.gateway.IsvAggregatePayConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvAggregatePayConfigConvert {
    IsvAggregatePayConfigConvert CONVERT = Mappers.getMapper(IsvAggregatePayConfigConvert.class);

    IsvAggregatePayConfig toEntity(IsvAggregatePayConfigParam param);

    IsvAggregatePayConfigResult toResult(IsvAggregatePayConfig aggregatePayConfig);

    IsvAggregateBarPayConfigResult toResult(IsvAggregateBarPayConfig aggregateBarPayConfig);

    void copy(IsvAggregatePayConfigParam param, @MappingTarget IsvAggregatePayConfig aggregatePayConfig);

    void copy(IsvAggregateBarPayConfigParam param, @MappingTarget IsvAggregateBarPayConfig barPayConfig);
}
