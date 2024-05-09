package cn.daxpay.single.service.core.system.config.convert;

import cn.daxpay.single.service.core.system.config.entity.PayChannelConfig;
import cn.daxpay.single.service.dto.system.config.PayChannelConfigDto;
import cn.daxpay.single.service.param.system.payinfo.PayChannelInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Mapper
public interface PayChannelConfigConvert {
    PayChannelConfigConvert CONVERT = Mappers.getMapper(PayChannelConfigConvert.class);

    PayChannelConfig convert(PayChannelInfoParam in);

    PayChannelConfigDto convert(PayChannelConfig in);
}
