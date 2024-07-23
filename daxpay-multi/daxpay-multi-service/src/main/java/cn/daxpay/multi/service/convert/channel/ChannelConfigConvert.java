package cn.daxpay.multi.service.convert.channel;

import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.daxpay.multi.service.result.channel.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
