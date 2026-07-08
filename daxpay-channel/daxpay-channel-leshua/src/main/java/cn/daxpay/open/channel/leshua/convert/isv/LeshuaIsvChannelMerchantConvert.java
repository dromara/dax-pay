package cn.daxpay.open.channel.leshua.convert.isv;

import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 乐刷通道商户绑定转换
///
@Mapper
public interface LeshuaIsvChannelMerchantConvert {

    LeshuaIsvChannelMerchantConvert CONVERT = Mappers.getMapper(LeshuaIsvChannelMerchantConvert.class);

    LeshuaIsvChannelMerchantResult toResult(LeshuaIsvChannelMerchant entity);
}
