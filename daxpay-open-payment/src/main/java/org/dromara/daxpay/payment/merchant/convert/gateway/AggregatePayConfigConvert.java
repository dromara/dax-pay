package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregateBarPayConfig;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregatePayConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateBarPayConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateQrPayConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateQrPayConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.AggregateBarPayConfigResult;
import org.dromara.daxpay.payment.merchant.result.gateway.AggregateQrPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface AggregatePayConfigConvert {
    AggregatePayConfigConvert CONVERT = Mappers.getMapper(AggregatePayConfigConvert.class);

    AggregateQrPayConfigResult toResult(AggregateQrPayConfig aggregatePayConfig);

    AggregateQrPayConfig toEntity(AggregateQrPayConfigParam param);

    AggregateQrPayConfig toEntity(IsvAggregatePayConfig o);

    void copy(AggregateQrPayConfigParam param, @MappingTarget AggregateQrPayConfig aggregatePayConfig);

    AggregateBarPayConfig toEntity(AggregateBarPayConfigParam param);

    AggregateBarPayConfigResult toResult(AggregateBarPayConfig aggregateBarPayConfig);

    AggregateBarPayConfig toEntity(IsvAggregateBarPayConfig aggregateBarPayConfig);

    void copy(AggregateBarPayConfigParam param, @MappingTarget AggregateBarPayConfig barPayConfig);
}
