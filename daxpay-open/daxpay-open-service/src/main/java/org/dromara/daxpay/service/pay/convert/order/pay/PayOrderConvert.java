package org.dromara.daxpay.service.pay.convert.order.pay;

import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.service.pay.result.order.pay.PayOrderExpandResult;
import org.dromara.daxpay.service.pay.result.order.pay.PayOrderVo;
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

    PayOrderExpandResult toResult(PayOrderExpand payOrder);
}
