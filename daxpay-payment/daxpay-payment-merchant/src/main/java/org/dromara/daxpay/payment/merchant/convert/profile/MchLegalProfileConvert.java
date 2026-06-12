package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchLegalProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchLegalProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchLegalProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户法人信息转换
///
@Mapper
public interface MchLegalProfileConvert {
    MchLegalProfileConvert CONVERT = Mappers.getMapper(MchLegalProfileConvert.class);

    MchLegalProfileResult toResult(MchLegalProfile entity);

    MchLegalProfile toEntity(MchLegalProfileParam param);
    
    void copy(MchLegalProfileParam param, @MappingTarget MchLegalProfile entity);
}