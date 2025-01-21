package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.core.result.checkout.CheckoutAggregateConfigResult;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.dromara.daxpay.service.param.config.checkout.CheckoutAggregateConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutAggregateConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/27
 */
@Mapper
public interface CheckoutAggregateConfigConvert {
    CheckoutAggregateConfigConvert CONVERT = Mappers.getMapper(CheckoutAggregateConfigConvert.class);

    CheckoutAggregateConfigResult toResult(CheckoutAggregateConfig param);

    CheckoutAggregateConfigVo toVo(CheckoutAggregateConfig param);

    CheckoutAggregateConfig toEntity(CheckoutAggregateConfigParam param);
}
