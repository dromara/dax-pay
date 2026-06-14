package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayDirectChannelMerchant;
import org.dromara.daxpay.channel.alipay.result.config.AlipayDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlipayDirectChannelMerchantConvert {

    AlipayDirectChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayDirectChannelMerchantConvert.class);

    AlipayDirectChannelMerchantResult toResult(AlipayDirectChannelMerchant entity);
}
