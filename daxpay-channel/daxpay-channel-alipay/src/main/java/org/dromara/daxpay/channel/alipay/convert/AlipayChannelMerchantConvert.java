package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayChannelMerchant;
import org.dromara.daxpay.channel.alipay.result.config.AlipayChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlipayChannelMerchantConvert {

    AlipayChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayChannelMerchantConvert.class);

    AlipayChannelMerchantResult toResult(AlipayChannelMerchant entity);
}
