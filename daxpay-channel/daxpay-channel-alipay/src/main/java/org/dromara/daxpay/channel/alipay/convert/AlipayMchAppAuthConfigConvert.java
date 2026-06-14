package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用授权认证配置转换
///
@Mapper
public interface AlipayMchAppAuthConfigConvert {

    AlipayMchAppAuthConfigConvert CONVERT = Mappers.getMapper(AlipayMchAppAuthConfigConvert.class);

    /// 转换为返回对象
    AlipayMchAppAuthConfigResult toResult(AlipayMchAppAuthConfig entity);

    /// 转换为实体
    AlipayMchAppAuthConfig toEntity(AlipayMchAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayMchAppAuthConfigParam param, @MappingTarget AlipayMchAppAuthConfig entity);
}
