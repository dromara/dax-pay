package cn.daxpay.open.channel.mock.convert.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.channel.mock.result.isv.MockIsvChannelMerchantResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # Mock 模拟服务商通道商户绑定转换
///
@Mapper
public interface MockIsvChannelMerchantConvert {

    MockIsvChannelMerchantConvert CONVERT = Mappers.getMapper(MockIsvChannelMerchantConvert.class);

    MockIsvChannelMerchantResult toResult(MockIsvChannelMerchant entity);
}
