package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商应用授权认证配置转换
///
@Mapper
public interface AlipayIsvAppAuthConfigConvert {

    AlipayIsvAppAuthConfigConvert CONVERT = Mappers.getMapper(AlipayIsvAppAuthConfigConvert.class);

    /// 转换为返回对象
    AlipayIsvAppAuthConfigResult toResult(AlipayIsvAppAuthConfig entity);

    /// 转换为实体
    AlipayIsvAppAuthConfig toEntity(AlipayIsvAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayIsvAppAuthConfigParam param, @MappingTarget AlipayIsvAppAuthConfig entity);
}
