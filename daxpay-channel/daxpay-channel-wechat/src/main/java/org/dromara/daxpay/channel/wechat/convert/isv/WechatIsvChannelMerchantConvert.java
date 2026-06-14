package org.dromara.daxpay.channel.wechat.convert.isv;

import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信服务商通道商户绑定转换
///
@Mapper
public interface WechatIsvChannelMerchantConvert {

    WechatIsvChannelMerchantConvert CONVERT = Mappers.getMapper(WechatIsvChannelMerchantConvert.class);

    WechatIsvChannelMerchantResult toResult(WechatIsvChannelMerchant entity);
}
