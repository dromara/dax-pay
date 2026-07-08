package cn.daxpay.open.channel.hmpay.convert.isv;

import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 河马付通道商户绑定转换
@Mapper
public interface HmpayIsvChannelMerchantConvert {

    HmpayIsvChannelMerchantConvert CONVERT = Mappers.getMapper(HmpayIsvChannelMerchantConvert.class);

    HmpayIsvChannelMerchantResult toResult(HmpayIsvChannelMerchant entity);
}
