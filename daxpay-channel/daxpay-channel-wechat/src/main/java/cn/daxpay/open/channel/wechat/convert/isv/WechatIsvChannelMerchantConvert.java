package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信服务商通道商户绑定转换
///
@Mapper
public interface WechatIsvChannelMerchantConvert {

    WechatIsvChannelMerchantConvert CONVERT = Mappers.getMapper(WechatIsvChannelMerchantConvert.class);

    WechatIsvChannelMerchantResult toResult(WechatIsvChannelMerchant entity);
}
