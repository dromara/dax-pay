package cn.daxpay.open.channel.stripe.convert;

import cn.daxpay.open.channel.stripe.entity.StripeChannelMerchant;
import cn.daxpay.open.channel.stripe.result.StripeChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # Stripe 通道商户转换
///
@Mapper
public interface StripeChannelMerchantConvert {
    StripeChannelMerchantConvert CONVERT = Mappers.getMapper(StripeChannelMerchantConvert.class);

    StripeChannelMerchantResult toResult(StripeChannelMerchant entity);
}
