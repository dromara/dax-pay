package cn.bootx.platform.daxpay.core.channel.alipay.convert;

import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.dto.channel.alipay.AliPayConfigDto;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayConfigParam;
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

    AliPayConfig convert(AliPayConfigDto in);

    AliPayConfig convert(AliPayConfigParam in);

    AliPayConfigDto convert(AliPayConfig in);

}
