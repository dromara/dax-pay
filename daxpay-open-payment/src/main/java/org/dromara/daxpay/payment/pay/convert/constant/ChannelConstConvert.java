package org.dromara.daxpay.payment.pay.convert.constant;

import org.dromara.daxpay.payment.pay.entity.constant.ChannelConst;
import org.dromara.daxpay.payment.pay.result.constant.ChannelConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface ChannelConstConvert {
    ChannelConstConvert CONVERT = Mappers.getMapper(ChannelConstConvert.class);

    ChannelConstResult toResult(ChannelConst source);
}
