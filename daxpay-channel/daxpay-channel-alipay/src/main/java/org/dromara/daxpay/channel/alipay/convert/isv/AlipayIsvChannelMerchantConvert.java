package org.dromara.daxpay.channel.alipay.convert.isv;

import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商通道商户绑定转换
///
@Mapper
public interface AlipayIsvChannelMerchantConvert {

    AlipayIsvChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayIsvChannelMerchantConvert.class);

    AlipayIsvChannelMerchantResult toResult(AlipayIsvChannelMerchant entity);
}
