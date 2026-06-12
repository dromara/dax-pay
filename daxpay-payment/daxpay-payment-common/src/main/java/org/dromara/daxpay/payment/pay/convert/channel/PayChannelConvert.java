package org.dromara.daxpay.payment.pay.convert.channel;

import org.dromara.daxpay.payment.pay.entity.masterdata.channel.PayChannel;
import org.dromara.daxpay.payment.pay.result.masterdata.channel.PayChannelResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道转换
///
@Mapper
public interface PayChannelConvert {
    PayChannelConvert CONVERT = Mappers.getMapper(PayChannelConvert.class);

    PayChannelResult toResult(PayChannel entity);
}