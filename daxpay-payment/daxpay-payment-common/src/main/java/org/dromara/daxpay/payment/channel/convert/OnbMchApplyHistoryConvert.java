package org.dromara.daxpay.payment.channel.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OnbMchApplyHistoryConvert {
    OnbMchApplyConvert CONVERT = Mappers.getMapper(OnbMchApplyConvert.class);
}
