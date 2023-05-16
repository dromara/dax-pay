package cn.bootx.platform.daxpay.core.pay.convert;

import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.param.pay.PayModeParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付对象转换
 *
 * @author xxm
 * @date 2022/3/2
 */
@Mapper
public interface PayConvert {

    PayConvert CONVERT = Mappers.getMapper(PayConvert.class);

    PayChannelInfo convert(PayModeParam in);

}
