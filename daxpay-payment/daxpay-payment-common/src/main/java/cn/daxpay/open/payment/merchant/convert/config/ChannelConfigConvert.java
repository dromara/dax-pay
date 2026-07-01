package cn.daxpay.open.payment.merchant.convert.config;

import cn.daxpay.open.payment.masterdata.config.entity.ChannelConfig;
import cn.daxpay.open.payment.merchant.result.config.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道配置
///
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert CONVERT = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
