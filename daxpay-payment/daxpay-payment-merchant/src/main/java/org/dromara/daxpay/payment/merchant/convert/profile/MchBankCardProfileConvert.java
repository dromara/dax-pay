package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchBankCardProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchBankCardProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBankCardProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户结算卡信息转换
///
@Mapper
public interface MchBankCardProfileConvert {
    MchBankCardProfileConvert CONVERT = Mappers.getMapper(MchBankCardProfileConvert.class);

    MchBankCardProfileResult toResult(MchBankCardProfile entity);

    MchBankCardProfile toEntity(MchBankCardProfileParam param);
    
    void copy(MchBankCardProfileParam param, @MappingTarget MchBankCardProfile entity);
}
