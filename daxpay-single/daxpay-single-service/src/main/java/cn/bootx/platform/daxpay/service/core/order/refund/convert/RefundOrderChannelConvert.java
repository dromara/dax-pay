package cn.bootx.platform.daxpay.service.core.order.refund.convert;

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

}
