package cn.daxpay.open.payment.masterdata.constants.capability.convert;

import cn.daxpay.open.payment.masterdata.constants.capability.entity.PayCapability;
import cn.daxpay.open.payment.masterdata.constants.capability.result.PayCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付能力转换
@Mapper
public interface PayCapabilityConvert {
    PayCapabilityConvert CONVERT = Mappers.getMapper(PayCapabilityConvert.class);

    PayCapabilityResult toResult(PayCapability entity);
}
