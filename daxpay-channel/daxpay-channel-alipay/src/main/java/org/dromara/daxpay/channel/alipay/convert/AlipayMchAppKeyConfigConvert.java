package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用密钥配置转换
///
@Mapper
public interface AlipayMchAppKeyConfigConvert {

    AlipayMchAppKeyConfigConvert CONVERT = Mappers.getMapper(AlipayMchAppKeyConfigConvert.class);

    /// 转换为返回对象
    AlipayMchAppKeyConfigResult toResult(AlipayMchAppKeyConfig entity);

    /// 转换为实体
    AlipayMchAppKeyConfig toEntity(AlipayMchAppKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖,密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayMchAppKeyConfigParam param, @MappingTarget AlipayMchAppKeyConfig entity);
}
