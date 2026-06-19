package cn.daxpay.open.channel.wechat.convert.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信直连通道商户绑定转换
///
@Mapper
public interface WechatDirectChannelMerchantConvert {

    WechatDirectChannelMerchantConvert CONVERT = Mappers.getMapper(WechatDirectChannelMerchantConvert.class);

    WechatDirectChannelMerchantResult toResult(WechatDirectChannelMerchant entity);
}
