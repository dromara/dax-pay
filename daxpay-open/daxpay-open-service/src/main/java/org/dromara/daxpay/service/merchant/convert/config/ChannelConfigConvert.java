package org.dromara.daxpay.service.merchant.convert.config;

import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;
import org.dromara.daxpay.service.merchant.result.config.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert INSTANCE = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
