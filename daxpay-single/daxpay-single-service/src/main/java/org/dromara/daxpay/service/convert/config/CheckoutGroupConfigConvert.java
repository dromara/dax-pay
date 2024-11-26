package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.service.entity.config.checkout.CheckoutGroupConfig;
import org.dromara.daxpay.service.param.config.checkout.CheckoutGroupConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutGroupConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/26
 */
@Mapper
public interface CheckoutGroupConfigConvert {
    CheckoutGroupConfigConvert CONVERT = Mappers.getMapper(CheckoutGroupConfigConvert.class);

    CheckoutGroupConfig toEntity(CheckoutGroupConfigParam param);

    CheckoutGroupConfigResult toResult(CheckoutGroupConfig entity);


}
