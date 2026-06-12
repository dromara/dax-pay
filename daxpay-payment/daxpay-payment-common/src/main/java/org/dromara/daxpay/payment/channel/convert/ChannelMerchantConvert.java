package org.dromara.daxpay.payment.channel.convert;

import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;
import org.dromara.daxpay.payment.channel.param.mch.ChannelMerchantGenParam;
import org.dromara.daxpay.payment.channel.result.info.ChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道商户转换
///
@Mapper
public interface ChannelMerchantConvert {
    ChannelMerchantConvert CONVERT = Mappers.getMapper(ChannelMerchantConvert.class);

    ChannelMerchantResult toResult(ChannelMerchant info);

    ChannelMerchant toEntity(ChannelMerchantGenParam result);
}
