package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvConfig;
import org.dromara.daxpay.channel.alipay.entity.config.AlipaySubConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvConfigParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipaySubConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AlipayConfigConvert {
    AlipayConfigConvert CONVERT = Mappers.getMapper(AlipayConfigConvert.class);

    AlipayConfigResult toResult(AliPayConfig in);

    AliPayConfig copy(AliPayConfig in);

    AliPayConfig toEntity(AlipayConfigParam in);

    void copy(AlipayConfigParam param, @MappingTarget AliPayConfig payConfig);

    void copy(AlipaySubConfigParam param, @MappingTarget AlipaySubConfig subConfig);

    void copy(AlipayIsvConfigParam param, @MappingTarget AlipayIsvConfig alipayIsvConfig);

}
