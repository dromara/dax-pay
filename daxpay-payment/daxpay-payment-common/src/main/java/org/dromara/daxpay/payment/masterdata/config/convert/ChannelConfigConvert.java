package org.dromara.daxpay.payment.masterdata.config.convert;

import org.dromara.daxpay.payment.masterdata.config.entity.ChannelConfig;
import org.dromara.daxpay.payment.masterdata.config.result.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道配置
///
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert INSTANCE = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
