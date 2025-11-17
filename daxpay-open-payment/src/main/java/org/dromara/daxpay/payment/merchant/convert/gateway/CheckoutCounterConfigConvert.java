package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvCheckoutCounterConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvCheckoutCounterConfigParam;
import org.dromara.daxpay.payment.merchant.entity.gateway.CheckoutCounterConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.CheckoutCounterConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.CheckoutCounterConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台配置项转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface CheckoutCounterConfigConvert {
    CheckoutCounterConfigConvert CONVERT = Mappers.getMapper(CheckoutCounterConfigConvert.class);


    CheckoutCounterConfig toEntity(CheckoutCounterConfigParam param);

    CheckoutCounterConfigResult toResult(CheckoutCounterConfig gatewayCheckoutCounterConfig);

    CheckoutCounterConfig toEntity(IsvCheckoutCounterConfig param);

    void copy(CheckoutCounterConfigParam param, @MappingTarget CheckoutCounterConfig checkoutCounterConfig);

    void copy(IsvCheckoutCounterConfigParam param, @MappingTarget CheckoutCounterConfig config);

}
