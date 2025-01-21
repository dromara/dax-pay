package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.core.result.checkout.CheckoutConfigResult;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;
import org.dromara.daxpay.service.param.config.checkout.CheckoutConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/27
 */
@Mapper
public interface CheckoutConfigConvert {
    CheckoutConfigConvert CONVERT = Mappers.getMapper(CheckoutConfigConvert.class);

    CheckoutConfigResult toResult(CheckoutConfig in);

    CheckoutConfigVo toVo(CheckoutConfig in);

    CheckoutConfig toEntity(CheckoutConfigParam in);


}
