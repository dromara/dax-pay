package cn.daxpay.open.payment.masterdata.convert.channel;

import cn.daxpay.open.payment.masterdata.entity.channel.PayChannel;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道转换
///
@Mapper
public interface PayChannelConvert {
    PayChannelConvert CONVERT = Mappers.getMapper(PayChannelConvert.class);

    PayChannelResult toResult(PayChannel entity);
}