package cn.daxpay.open.channel.hkrt.convert.isv;

import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 海科融通通道商户绑定转换
///
@Mapper
public interface HkrtIsvChannelMerchantConvert {

    HkrtIsvChannelMerchantConvert CONVERT = Mappers.getMapper(HkrtIsvChannelMerchantConvert.class);

    HkrtIsvChannelMerchantResult toResult(HkrtIsvChannelMerchant entity);
}
