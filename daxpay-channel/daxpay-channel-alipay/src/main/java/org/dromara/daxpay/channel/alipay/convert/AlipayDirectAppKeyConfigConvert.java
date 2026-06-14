package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用密钥配置转换
///
/// MapStruct转换器，负责支付宝直连商户应用密钥配置在实体、参数和返回结果之间的转换，更新时空值不覆盖。
///
@Mapper
public interface AlipayDirectAppKeyConfigConvert {

    AlipayDirectAppKeyConfigConvert CONVERT = Mappers.getMapper(AlipayDirectAppKeyConfigConvert.class);

    /// 转换为返回对象
    AlipayDirectAppKeyConfigResult toResult(AlipayDirectAppKeyConfig entity);

    /// 转换为实体
    AlipayDirectAppKeyConfig toEntity(AlipayDirectAppKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖,密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayDirectAppKeyConfigParam param, @MappingTarget AlipayDirectAppKeyConfig entity);
}
