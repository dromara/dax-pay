package cn.daxpay.open.channel.vbill.convert.isv;

import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 随行付通道商户绑定转换
@Mapper
public interface VbillIsvChannelMerchantConvert {

    VbillIsvChannelMerchantConvert CONVERT = Mappers.getMapper(VbillIsvChannelMerchantConvert.class);

    VbillIsvChannelMerchantResult toResult(VbillIsvChannelMerchant entity);
}
