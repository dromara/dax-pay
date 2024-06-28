package cn.daxpay.multi.service.convert.order.pay;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.result.order.pay.PayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderResult toResult(PayOrder payOrder);
}
