package cn.daxpay.open.channel.douyin.convert.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 抖音直连通道商户转换
///
@Mapper
public interface DouyinDirectChannelMerchantConvert {
    DouyinDirectChannelMerchantConvert CONVERT = Mappers.getMapper(DouyinDirectChannelMerchantConvert.class);

    DouyinDirectChannelMerchantResult toResult(DouyinDirectChannelMerchant entity);
}
