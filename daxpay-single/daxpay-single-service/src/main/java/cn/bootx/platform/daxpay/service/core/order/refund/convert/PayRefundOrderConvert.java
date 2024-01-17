package cn.bootx.platform.daxpay.service.core.order.refund.convert;

import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.dto.order.refund.PayRefundOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface PayRefundOrderConvert {

    PayRefundOrderConvert CONVERT = Mappers.getMapper(PayRefundOrderConvert.class);

    PayRefundOrderDto convert(PayRefundOrder in);

    RefundOrderResult convertResult(PayRefundOrder in);


}
