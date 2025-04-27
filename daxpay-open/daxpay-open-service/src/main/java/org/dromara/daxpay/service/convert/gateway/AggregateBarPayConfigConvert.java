package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.AggregateBarPayConfig;
import org.dromara.daxpay.service.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.AggregateBarPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 聚合支付码支付
 * @author xxm
 * @since 2025/3/21
 */
@Mapper
public interface AggregateBarPayConfigConvert {
    AggregateBarPayConfigConvert CONVERT = Mappers.getMapper(AggregateBarPayConfigConvert.class);

    AggregateBarPayConfig toEntity(AggregateBarPayConfigParam param);

    AggregateBarPayConfigResult toResult(AggregateBarPayConfig aggregateBarPayConfig);
}
