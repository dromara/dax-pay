package cn.bootx.platform.daxpay.service.core.system.payinfo.convert;

import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayChannelInfo;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayChannelInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayChannelInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Mapper
public interface PayChannelInfoConvert {
    PayChannelInfoConvert CONVERT = Mappers.getMapper(PayChannelInfoConvert.class);

    PayChannelInfo convert(PayChannelInfoParam in);

    PayChannelInfoDto convert(PayChannelInfo in);
}
