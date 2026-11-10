package cn.daxpay.open.channel.mock.convert.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # Mock 模拟服务商信息转换
///
@Mapper
public interface MockIsvInfoConvert {

    MockIsvInfoConvert CONVERT = Mappers.getMapper(MockIsvInfoConvert.class);

    /// 转换为返回对象
    MockIsvInfoResult toResult(MockIsvInfo entity);
}
