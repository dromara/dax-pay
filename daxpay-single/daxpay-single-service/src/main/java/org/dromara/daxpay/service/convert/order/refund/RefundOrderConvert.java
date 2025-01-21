package org.dromara.daxpay.service.convert.order.refund;

import org.dromara.daxpay.core.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.result.order.refund.RefundOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrderVo toVo(RefundOrder in);

    RefundOrderResult toResult(RefundOrder in);
}
