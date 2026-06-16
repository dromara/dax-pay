package org.dromara.daxpay.payment.old.pay.convert.config;

import org.dromara.daxpay.payment.old.pay.entity.config.ChannelConfig;
import org.dromara.daxpay.payment.old.pay.result.config.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道配置
///
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert INSTANCE = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
