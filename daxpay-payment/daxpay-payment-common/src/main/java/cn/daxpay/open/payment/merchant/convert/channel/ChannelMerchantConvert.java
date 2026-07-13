package cn.daxpay.open.payment.merchant.convert.channel;

import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantGenParam;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
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
