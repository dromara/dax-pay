package cn.daxpay.single.service.core.order.pay.convert;

import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import cn.daxpay.single.service.dto.order.pay.PayOrderDto;
import cn.daxpay.single.service.dto.order.pay.PayOrderExtraDto;
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
