package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连通道商户绑定转换
///
@Mapper
public interface AlipayDirectChannelMerchantConvert {

    AlipayDirectChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayDirectChannelMerchantConvert.class);

    AlipayDirectChannelMerchantResult toResult(AlipayDirectChannelMerchant entity);
}
