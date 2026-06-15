package org.dromara.daxpay.channel.douyin.convert.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 抖音直连通道商户转换
///
@Mapper
public interface DouyinDirectChannelMerchantConvert {
    DouyinDirectChannelMerchantConvert CONVERT = Mappers.getMapper(DouyinDirectChannelMerchantConvert.class);

    DouyinDirectChannelMerchantResult toResult(DouyinDirectChannelMerchant entity);
}
