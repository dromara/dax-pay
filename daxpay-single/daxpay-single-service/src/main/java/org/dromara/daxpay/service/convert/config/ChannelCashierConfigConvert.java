package org.dromara.daxpay.service.convert.config;

import org.dromara.daxpay.service.entity.config.ChannelCashierConfig;
import org.dromara.daxpay.service.param.config.ChannelCashierConfigParam;
import org.dromara.daxpay.service.result.config.ChannelCashierConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Mapper
public interface ChannelCashierConfigConvert {
    ChannelCashierConfigConvert CONVERT = Mappers.getMapper(ChannelCashierConfigConvert.class);

    ChannelCashierConfig toEntity(ChannelCashierConfigParam param);

    ChannelCashierConfigResult toResult(ChannelCashierConfig channelCashierConfig);
}
