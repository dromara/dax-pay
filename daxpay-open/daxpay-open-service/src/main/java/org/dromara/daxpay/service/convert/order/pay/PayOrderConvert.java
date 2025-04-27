package org.dromara.daxpay.service.convert.order.pay;

import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.result.order.pay.PayOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付订单
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderVo toVo(PayOrder payOrder);

    PayOrderResult toResult(PayOrder payOrder);
}
