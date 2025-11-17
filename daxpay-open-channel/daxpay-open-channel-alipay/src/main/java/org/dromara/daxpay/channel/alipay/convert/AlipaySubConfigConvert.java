package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipaySubConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipaySubConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipaySubConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface AlipaySubConfigConvert {
    AlipaySubConfigConvert CONVERT = Mappers.getMapper(AlipaySubConfigConvert.class);

    AlipaySubConfigResult toResult(AlipaySubConfig in);

    AlipaySubConfig copy(AlipaySubConfig in);

    AlipaySubConfig toEntity(AlipaySubConfigParam in);
}
