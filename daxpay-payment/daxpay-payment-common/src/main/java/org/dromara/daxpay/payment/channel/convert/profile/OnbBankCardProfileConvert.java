package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbBankCardProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbBankCardProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件银行账户信息转换
///
@Mapper
public interface OnbBankCardProfileConvert {
    OnbBankCardProfileConvert CONVERT = Mappers.getMapper(OnbBankCardProfileConvert.class);

    OnbBankCardProfile toEntity(OnbBankCardProfileBo param);

    OnbBankCardProfileBo toResult(OnbBankCardProfile entity);
    
    void copy(OnbBankCardProfileBo param, @MappingTarget OnbBankCardProfile entity);
}
