package org.dromara.daxpay.payment.masterdata.constants.channel.convert;

import org.dromara.daxpay.payment.masterdata.constants.channel.entity.PayChannel;
import org.dromara.daxpay.payment.masterdata.constants.channel.result.PayChannelResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道转换
///
@Mapper
public interface PayChannelConvert {
    PayChannelConvert CONVERT = Mappers.getMapper(PayChannelConvert.class);

    PayChannelResult toResult(PayChannel entity);
}