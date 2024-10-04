package org.dromara.daxpay.channel.alipay.convert.config;

import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.param.config.AliPayConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface AlipayConfigConvert {
    AlipayConfigConvert CONVERT = Mappers.getMapper(AlipayConfigConvert.class);

    AlipayConfigResult toResult(AliPayConfig in);

    AliPayConfig copy(AliPayConfig in);

    AliPayConfig toEntity(AliPayConfigParam in);
}
