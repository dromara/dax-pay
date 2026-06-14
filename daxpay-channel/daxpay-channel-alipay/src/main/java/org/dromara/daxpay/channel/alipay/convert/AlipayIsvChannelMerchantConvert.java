package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvChannelMerchant;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlipayIsvChannelMerchantConvert {

    AlipayIsvChannelMerchantConvert CONVERT = Mappers.getMapper(AlipayIsvChannelMerchantConvert.class);

    /// 实体 appId(Long) 映射到结果 isvAppId(Long), 避免与父类 MchTradeBaseResult.appId(String) 冲突
    @Mapping(source = "appId", target = "isvAppId")
    AlipayIsvChannelMerchantResult toResult(AlipayIsvChannelMerchant entity);
}
