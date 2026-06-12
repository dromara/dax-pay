package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbCardHolderProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbCardHolderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件持卡人信息转换
///
@Mapper
public interface OnbCardHolderProfileConvert {
    OnbCardHolderProfileConvert CONVERT = Mappers.getMapper(OnbCardHolderProfileConvert.class);

    OnbCardHolderProfile toEntity(OnbCardHolderProfileBo param);

    OnbCardHolderProfileBo toResult(OnbCardHolderProfile entity);
    
    void copy(OnbCardHolderProfileBo param, @MappingTarget OnbCardHolderProfile entity);
}
