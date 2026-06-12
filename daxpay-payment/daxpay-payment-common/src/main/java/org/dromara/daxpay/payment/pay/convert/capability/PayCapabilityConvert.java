package org.dromara.daxpay.payment.pay.convert.capability;

import org.dromara.daxpay.payment.pay.entity.masterdata.capability.PayCapability;
import org.dromara.daxpay.payment.pay.result.masterdata.capability.PayCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付能力转换
@Mapper
public interface PayCapabilityConvert {
    PayCapabilityConvert CONVERT = Mappers.getMapper(PayCapabilityConvert.class);

    PayCapabilityResult toResult(PayCapability entity);
}
