package org.dromara.daxpay.payment.merchant.convert.config;

import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.pay.entity.config.ChannelConfig;
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
