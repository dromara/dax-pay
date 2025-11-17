package org.dromara.daxpay.payment.merchant.convert.gateway;

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


}
