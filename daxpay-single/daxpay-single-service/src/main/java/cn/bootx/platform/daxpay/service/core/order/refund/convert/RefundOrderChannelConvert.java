package cn.bootx.platform.daxpay.service.core.order.refund.convert;

import cn.bootx.platform.daxpay.result.order.RefundChannelOrderResult;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundChannelOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/17
 */
@Mapper
public interface RefundOrderChannelConvert {
    RefundOrderChannelConvert CONVERT = Mappers.getMapper(RefundOrderChannelConvert.class);


    RefundChannelOrderDto convert(PayRefundChannelOrder in);

    RefundChannelOrderResult convertResult(PayRefundChannelOrder in);

}
