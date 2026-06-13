package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvAppKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商应用密钥配置转换
///
@Mapper
public interface AlipayIsvAppKeyConfigConvert {

    AlipayIsvAppKeyConfigConvert CONVERT = Mappers.getMapper(AlipayIsvAppKeyConfigConvert.class);

    /// 转换为返回对象
    AlipayIsvAppKeyConfigResult toResult(AlipayIsvAppKeyConfig entity);

    /// 转换为实体
    AlipayIsvAppKeyConfig toEntity(AlipayIsvAppKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖,密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayIsvAppKeyConfigParam param, @MappingTarget AlipayIsvAppKeyConfig entity);
}
