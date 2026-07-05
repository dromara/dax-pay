package cn.daxpay.open.channel.lakala.convert.isv;

import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 拉卡拉通道商户绑定转换
///
@Mapper
public interface LakalaIsvChannelMerchantConvert {

    LakalaIsvChannelMerchantConvert CONVERT = Mappers.getMapper(LakalaIsvChannelMerchantConvert.class);

    LakalaIsvChannelMerchantResult toResult(LakalaIsvChannelMerchant entity);
}
