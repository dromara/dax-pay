package cn.daxpay.multi.channel.alipay.convert;

import cn.daxpay.multi.channel.alipay.entity.AlipayConfig;
import cn.daxpay.multi.channel.alipay.param.AlipayConfigParam;
import cn.daxpay.multi.channel.alipay.result.AlipayConfigResult;
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

    AlipayConfigResult toResult(AlipayConfig in);

    AlipayConfig copy(AlipayConfig in);

    AlipayConfig toEntity(AlipayConfigParam in);
}
