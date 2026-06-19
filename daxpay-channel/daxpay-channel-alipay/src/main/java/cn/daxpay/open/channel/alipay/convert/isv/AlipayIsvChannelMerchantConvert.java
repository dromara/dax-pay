package cn.daxpay.open.channel.alipay.convert.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商通道商户绑定转换
///
@Mapper
public interface AlipayIsvChannelMerchantConvert {

    AlipayIsvChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayIsvChannelMerchantConvert.class);

    AlipayIsvChannelMerchantResult toResult(AlipayIsvChannelMerchant entity);
}
