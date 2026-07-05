package cn.daxpay.open.channel.ums.convert.direct;

import cn.daxpay.open.channel.ums.entity.direct.UmsDirectChannelMerchant;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 银联商务直连通道商户绑定转换
@Mapper
public interface UmsDirectChannelMerchantConvert {
    UmsDirectChannelMerchantConvert CONVERT = Mappers.getMapper(UmsDirectChannelMerchantConvert.class);

    UmsDirectChannelMerchantResult toResult(UmsDirectChannelMerchant entity);
}
