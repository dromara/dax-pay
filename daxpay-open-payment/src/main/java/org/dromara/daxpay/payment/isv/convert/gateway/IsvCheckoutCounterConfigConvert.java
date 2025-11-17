package org.dromara.daxpay.payment.isv.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvCheckoutCounterConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvCheckoutCounterConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvCheckoutCounterConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 网关收银台配置项转换
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvCheckoutCounterConfigConvert {
    IsvCheckoutCounterConfigConvert CONVERT = Mappers.getMapper(IsvCheckoutCounterConfigConvert.class);

    IsvCheckoutCounterConfig toEntity(IsvCheckoutCounterConfigParam param);

    IsvCheckoutCounterConfigResult toResult(IsvCheckoutCounterConfig gatewayCashierItemConfig);

    void copy(IsvCheckoutCounterConfigParam param, @MappingTarget IsvCheckoutCounterConfig checkoutCounterConfig);
}
