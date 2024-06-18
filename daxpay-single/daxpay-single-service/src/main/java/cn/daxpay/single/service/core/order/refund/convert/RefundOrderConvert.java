package cn.daxpay.single.service.core.order.refund.convert;

import cn.daxpay.single.core.result.order.RefundOrderResult;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.notice.result.RefundNoticeResult;
import cn.daxpay.single.service.dto.order.refund.RefundOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrderDto convert(RefundOrder in);

    RefundOrderResult convertResult(RefundOrder in);

    RefundNoticeResult convertNotice(RefundOrder order);
}
