package org.dromara.daxpay.service.isv.convert.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregatePayConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregatePayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvAggregatePayConfigConvert {
    IsvAggregatePayConfigConvert CONVERT = Mappers.getMapper(IsvAggregatePayConfigConvert.class);

    IsvAggregatePayConfigResult toResult(IsvAggregatePayConfig aggregatePayConfig);

    IsvAggregatePayConfig toEntity(IsvAggregatePayConfigParam param);
}
