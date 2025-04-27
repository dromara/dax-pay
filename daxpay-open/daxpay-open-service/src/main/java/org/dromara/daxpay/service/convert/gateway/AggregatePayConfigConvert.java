package org.dromara.daxpay.service.convert.gateway;

import org.dromara.daxpay.service.entity.gateway.AggregatePayConfig;
import org.dromara.daxpay.service.param.gateway.AggregatePayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.AggregatePayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface AggregatePayConfigConvert {
    AggregatePayConfigConvert CONVERT = Mappers.getMapper(AggregatePayConfigConvert.class);

    AggregatePayConfigResult toResult(AggregatePayConfig aggregatePayConfig);

    AggregatePayConfig toEntity(AggregatePayConfigParam param);
}
