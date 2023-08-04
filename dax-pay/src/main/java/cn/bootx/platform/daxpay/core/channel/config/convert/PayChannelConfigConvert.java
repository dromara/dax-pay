package cn.bootx.platform.daxpay.core.channel.config.convert;

import cn.bootx.platform.daxpay.core.channel.config.entity.PayChannelConfig;
import cn.bootx.platform.daxpay.dto.channel.config.PayChannelConfigDto;
import cn.bootx.platform.daxpay.param.channel.config.PayChannelConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付渠道配置
 *
 * @author xxm
 * @since 2023-05-24
 */
@Mapper
public interface PayChannelConfigConvert {

    PayChannelConfigConvert CONVERT = Mappers.getMapper(PayChannelConfigConvert.class);

    PayChannelConfig convert(PayChannelConfigParam in);

    PayChannelConfigDto convert(PayChannelConfig in);

}
