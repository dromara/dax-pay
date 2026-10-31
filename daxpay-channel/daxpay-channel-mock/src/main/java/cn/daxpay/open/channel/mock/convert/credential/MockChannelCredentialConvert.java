package cn.daxpay.open.channel.mock.convert.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockChannelCredential;
import cn.daxpay.open.channel.mock.param.credential.MockChannelCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockChannelCredentialResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Mock 模拟通道网关签名密钥配置转换
///
/// MapStruct 转换器, 更新时空值不覆盖(密钥留空保留原值)。
@Mapper
public interface MockChannelCredentialConvert {

    MockChannelCredentialConvert CONVERT = Mappers.getMapper(MockChannelCredentialConvert.class);

    /// 转换为返回对象
    MockChannelCredentialResult toResult(MockChannelCredential entity);

    /// 更新参数到实体(空值不覆盖, 密钥为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MockChannelCredentialParam param, @MappingTarget MockChannelCredential entity);
}
