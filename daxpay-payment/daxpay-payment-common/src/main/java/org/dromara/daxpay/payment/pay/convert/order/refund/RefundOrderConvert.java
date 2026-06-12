package org.dromara.daxpay.payment.pay.convert.order.refund;

import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.pay.result.order.refund.RefundOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrderVo toVo(RefundOrder in);

    RefundOrderResult toResult(RefundOrder in);
}
