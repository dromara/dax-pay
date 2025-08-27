package org.dromara.daxpay.service.isv.convert.config;

import org.dromara.daxpay.service.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.service.isv.result.config.IsvChannelConfigResult;
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
