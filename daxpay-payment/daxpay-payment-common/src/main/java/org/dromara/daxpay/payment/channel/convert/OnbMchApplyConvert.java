package org.dromara.daxpay.payment.channel.convert;

import org.dromara.daxpay.payment.channel.entity.apply.OnbMchApply;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyParam;
import org.dromara.daxpay.payment.channel.result.apply.OnbMchApplyResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OnbMchApplyConvert {
    OnbMchApplyConvert CONVERT = Mappers.getMapper(OnbMchApplyConvert.class);

    OnbMchApplyResult toResult(OnbMchApply entity);

    OnbMchApply toEntity(OnbMchApplyParam result);
}
