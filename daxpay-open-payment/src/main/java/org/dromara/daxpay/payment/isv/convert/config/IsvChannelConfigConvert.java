package org.dromara.daxpay.payment.isv.convert.config;

import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.payment.isv.result.config.IsvChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/10/30
 */
@Mapper
public interface IsvChannelConfigConvert {
    IsvChannelConfigConvert CONVERT = Mappers.getMapper(IsvChannelConfigConvert.class);

    IsvChannelConfigResult toResult(IsvChannelConfig entity);

}
