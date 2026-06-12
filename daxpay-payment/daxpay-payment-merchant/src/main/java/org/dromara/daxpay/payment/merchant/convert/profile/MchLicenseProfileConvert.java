package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchLicenseProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchLicenseProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchLicenseProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户营业执照信息转换
///
@Mapper
public interface MchLicenseProfileConvert {
    MchLicenseProfileConvert CONVERT = Mappers.getMapper(MchLicenseProfileConvert.class);

    MchLicenseProfileResult toResult(MchLicenseProfile entity);

    MchLicenseProfile toEntity(MchLicenseProfileParam param);
    
    void copy(MchLicenseProfileParam param, @MappingTarget MchLicenseProfile entity);
}