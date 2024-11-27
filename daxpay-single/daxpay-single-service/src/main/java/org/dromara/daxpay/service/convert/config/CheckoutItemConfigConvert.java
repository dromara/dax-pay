package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.dromara.daxpay.service.param.config.checkout.CheckoutItemConfigParam;
import org.dromara.daxpay.core.result.checkout.CheckoutItemConfigResult;
import org.dromara.daxpay.service.result.config.checkout.CheckoutItemConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/26
 */
@Mapper
public interface CheckoutItemConfigConvert {
    CheckoutItemConfigConvert CONVERT = Mappers.getMapper(CheckoutItemConfigConvert.class);

    CheckoutItemConfig toEntity(CheckoutItemConfigParam param);

    CheckoutItemConfigResult toResult(CheckoutItemConfig entity);

    CheckoutItemConfigVo toVo(CheckoutItemConfig entity);
}
