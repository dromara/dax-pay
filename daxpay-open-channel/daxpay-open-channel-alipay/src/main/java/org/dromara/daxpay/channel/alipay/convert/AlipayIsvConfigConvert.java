package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/10/31
 */
@Mapper
public interface AlipayIsvConfigConvert {
    AlipayIsvConfigConvert CONVERT = Mappers.getMapper(AlipayIsvConfigConvert.class);

    AlipayIsvConfigResult toResult(AlipayIsvConfig config);

    AlipayIsvConfig copy(AlipayIsvConfig result);

    AlipayIsvConfig toEntity(AlipayIsvConfigParam param);
}
