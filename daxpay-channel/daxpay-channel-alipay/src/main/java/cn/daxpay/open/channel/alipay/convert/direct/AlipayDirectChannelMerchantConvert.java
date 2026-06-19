package cn.daxpay.open.channel.alipay.convert.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连通道商户绑定转换
///
@Mapper
public interface AlipayDirectChannelMerchantConvert {

    AlipayDirectChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayDirectChannelMerchantConvert.class);

    AlipayDirectChannelMerchantResult toResult(AlipayDirectChannelMerchant entity);
}
