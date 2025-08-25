package org.dromara.daxpay.service.isv.convert.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregateBarPayConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregateBarPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 聚合支付码支付
 * @author xxm
 * @since 2025/3/21
 */
@Mapper
public interface IsvAggregateBarPayConfigConvert {
    IsvAggregateBarPayConfigConvert CONVERT = Mappers.getMapper(IsvAggregateBarPayConfigConvert.class);

    IsvAggregateBarPayConfig toEntity(IsvAggregateBarPayConfigParam param);

    IsvAggregateBarPayConfigResult toResult(IsvAggregateBarPayConfig aggregateBarPayConfig);
}
