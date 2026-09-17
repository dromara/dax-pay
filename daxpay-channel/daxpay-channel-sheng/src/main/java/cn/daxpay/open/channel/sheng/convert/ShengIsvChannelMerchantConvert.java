package cn.daxpay.open.channel.sheng.convert;

import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.channel.sheng.result.ShengIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 盛付通服务商通道商户绑定转换
@Mapper
public interface ShengIsvChannelMerchantConvert {
    ShengIsvChannelMerchantConvert CONVERT = Mappers.getMapper(ShengIsvChannelMerchantConvert.class);

    ShengIsvChannelMerchantResult toResult(ShengIsvChannelMerchant entity);
}
