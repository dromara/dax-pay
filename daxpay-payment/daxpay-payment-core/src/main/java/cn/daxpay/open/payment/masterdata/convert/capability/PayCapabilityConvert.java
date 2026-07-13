package cn.daxpay.open.payment.masterdata.convert.capability;

import cn.daxpay.open.payment.masterdata.entity.capability.PayCapability;
import cn.daxpay.open.payment.masterdata.result.capability.PayCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付能力转换
@Mapper
public interface PayCapabilityConvert {
    PayCapabilityConvert CONVERT = Mappers.getMapper(PayCapabilityConvert.class);

    PayCapabilityResult toResult(PayCapability entity);
}
