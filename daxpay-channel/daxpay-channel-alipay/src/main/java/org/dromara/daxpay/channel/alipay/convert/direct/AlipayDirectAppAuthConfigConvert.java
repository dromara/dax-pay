package org.dromara.daxpay.channel.alipay.convert.direct;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用授权认证配置转换
///
/// MapStruct转换器，负责支付宝直连商户应用授权认证配置在实体、参数和返回结果之间的转换，更新时空值不覆盖。
///
@Mapper
public interface AlipayDirectAppAuthConfigConvert {

    AlipayDirectAppAuthConfigConvert CONVERT = Mappers.getMapper(AlipayDirectAppAuthConfigConvert.class);

    /// 转换为返回对象
    AlipayDirectAppAuthConfigResult toResult(AlipayDirectAppAuthConfig entity);

    /// 转换为实体
    AlipayDirectAppAuthConfig toEntity(AlipayDirectAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayDirectAppAuthConfigParam param, @MappingTarget AlipayDirectAppAuthConfig entity);
}
