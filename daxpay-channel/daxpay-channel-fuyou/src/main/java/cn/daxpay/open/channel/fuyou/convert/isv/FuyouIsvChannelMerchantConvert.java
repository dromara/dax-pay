package cn.daxpay.open.channel.fuyou.convert.isv;

import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 富友通道商户绑定转换
@Mapper
public interface FuyouIsvChannelMerchantConvert {

    FuyouIsvChannelMerchantConvert CONVERT = Mappers.getMapper(FuyouIsvChannelMerchantConvert.class);

    /// 转换为返回对象
    FuyouIsvChannelMerchantResult toResult(FuyouIsvChannelMerchant entity);
}
