package cn.daxpay.open.channel.mock.convert.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.param.isv.MockIsvInfoParam;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoListResult;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoOption;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Mock 模拟服务商信息转换
///
@Mapper
public interface MockIsvInfoConvert {

    MockIsvInfoConvert CONVERT = Mappers.getMapper(MockIsvInfoConvert.class);

    /// 转换为返回对象
    MockIsvInfoResult toResult(MockIsvInfo entity);

    /// 转换为下拉选项
    MockIsvInfoOption toOption(MockIsvInfo entity);

    /// 转换为列表行(名下通道商户计数由 Service 补充)
    MockIsvInfoListResult toListResult(MockIsvInfo entity);

    /// 更新源数据到实体(空值不覆盖, 供编辑+启停合一的 update 使用)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MockIsvInfoParam param, @MappingTarget MockIsvInfo entity);
}
