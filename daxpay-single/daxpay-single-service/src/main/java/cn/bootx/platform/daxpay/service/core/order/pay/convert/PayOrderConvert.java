package cn.bootx.platform.daxpay.service.core.order.pay.convert;

import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderExtraDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/9
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderExtraDto convert(PayOrderExtra in);

    PayOrderDto convert(PayOrder in);

}
