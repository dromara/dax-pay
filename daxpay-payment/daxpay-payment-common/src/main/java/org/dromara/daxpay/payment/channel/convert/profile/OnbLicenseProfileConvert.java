package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbLicenseProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbLicenseProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件营业执照信息转换
///
@Mapper
public interface OnbLicenseProfileConvert {
    OnbLicenseProfileConvert CONVERT = Mappers.getMapper(OnbLicenseProfileConvert.class);

    OnbLicenseProfile toEntity(OnbLicenseProfileBo param);

    OnbLicenseProfileBo toResult(OnbLicenseProfile entity);
    
    void copy(OnbLicenseProfileBo param, @MappingTarget OnbLicenseProfile entity);
}
