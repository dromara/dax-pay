package cn.daxpay.open.channel.mock.convert.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockDirectCredential;
import cn.daxpay.open.channel.mock.param.credential.MockDirectCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockDirectCredentialResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # Mock 模拟通道直连商户接入密钥转换
///
/// 仅对改名字段写 @Mapping(两段 PEM 脱敏出口/更新时间), 其余同名自动映射。
@Mapper
public interface MockDirectCredentialConvert {

    MockDirectCredentialConvert CONVERT = Mappers.getMapper(MockDirectCredentialConvert.class);

    /// 转换为脱敏视图(两段 PEM 改名 + 最近保存时间改名)
    @Mapping(source = "merchantPrivateKey", target = "merchantPrivateKeyMasked")
    @Mapping(source = "platformPublicKey", target = "platformPublicKeyMasked")
    @Mapping(source = "lastModifiedTime", target = "updateTime")
    MockDirectCredentialResult toResult(MockDirectCredential entity);

    /// 保存参数到实体(空值不覆盖, 两段 PEM 为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MockDirectCredentialParam param, @MappingTarget MockDirectCredential entity);
}
