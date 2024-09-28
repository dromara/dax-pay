package cn.daxpay.multi.service.convert.config;

import cn.daxpay.multi.service.entity.config.ChannelCashierConfig;
import cn.daxpay.multi.service.param.config.ChannelCashierConfigParam;
import cn.daxpay.multi.service.result.config.ChannelCashierConfigResult;
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
