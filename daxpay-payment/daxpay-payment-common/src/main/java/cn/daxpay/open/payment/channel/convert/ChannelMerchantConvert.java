package cn.daxpay.open.payment.channel.convert;

import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantGenParam;
import cn.daxpay.open.payment.channel.result.info.ChannelMerchantResult;
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
