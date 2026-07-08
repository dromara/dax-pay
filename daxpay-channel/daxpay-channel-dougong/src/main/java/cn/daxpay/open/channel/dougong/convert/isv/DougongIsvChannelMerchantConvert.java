package cn.daxpay.open.channel.dougong.convert.isv;

import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 斗拱通道商户绑定转换
@Mapper
public interface DougongIsvChannelMerchantConvert {

    DougongIsvChannelMerchantConvert CONVERT = Mappers.getMapper(DougongIsvChannelMerchantConvert.class);

    DougongIsvChannelMerchantResult toResult(DougongIsvChannelMerchant entity);
}
