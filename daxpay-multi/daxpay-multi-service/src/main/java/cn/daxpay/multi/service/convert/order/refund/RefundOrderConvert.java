package cn.daxpay.multi.service.convert.order.refund;

import cn.daxpay.multi.core.result.trade.refund.RefundOrderResult;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.result.order.refund.RefundOrderVo;
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
