package cn.bootx.platform.daxpay.core.channel.alipay.convert;

import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.dto.channel.alipay.AlipayConfigDto;
import cn.bootx.platform.daxpay.param.channel.alipay.AlipayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付宝转换
 *
 * @author xxm
 * @since 2021/7/5
 */
@Mapper
public interface AlipayConvert {

    AlipayConvert CONVERT = Mappers.getMapper(AlipayConvert.class);

    AlipayConfig convert(AlipayConfigDto in);

    AlipayConfig convert(AlipayConfigParam in);

    AlipayConfigDto convert(AlipayConfig in);

}
