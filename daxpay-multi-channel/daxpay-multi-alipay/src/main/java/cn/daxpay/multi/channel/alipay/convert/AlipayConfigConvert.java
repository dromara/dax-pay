package cn.daxpay.multi.channel.alipay.convert;

import cn.daxpay.multi.channel.alipay.entity.AliPayConfig;
import cn.daxpay.multi.channel.alipay.param.config.AlipayConfigParam;
import cn.daxpay.multi.channel.alipay.result.config.AlipayConfigResult;
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

    AliPayConfig toEntity(AlipayConfigParam in);
}
