package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbLegalProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbLegalProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件法人信息转换
///
@Mapper
public interface OnbLegalProfileConvert {
    OnbLegalProfileConvert CONVERT = Mappers.getMapper(OnbLegalProfileConvert.class);

    OnbLegalProfile toEntity(OnbLegalProfileBo param);

    OnbLegalProfileBo toResult(OnbLegalProfile entity);
    
    void copy(OnbLegalProfileBo param, @MappingTarget OnbLegalProfile entity);
}
