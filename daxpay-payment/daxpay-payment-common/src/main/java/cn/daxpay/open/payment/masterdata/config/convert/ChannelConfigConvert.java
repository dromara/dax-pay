package cn.daxpay.open.payment.masterdata.config.convert;

import cn.daxpay.open.payment.masterdata.config.entity.ChannelConfig;
import cn.daxpay.open.payment.masterdata.config.result.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道配置
///
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert INSTANCE = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
