package cn.bootx.platform.daxpay.service.core.order.refund.convert;

import cn.bootx.platform.daxpay.result.order.RefundOrderChannelResult;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrderChannel;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderChannelDto;
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


    RefundOrderChannelDto convert(PayRefundOrderChannel in);

    RefundOrderChannelResult convertResult(PayRefundOrderChannel in);

}
