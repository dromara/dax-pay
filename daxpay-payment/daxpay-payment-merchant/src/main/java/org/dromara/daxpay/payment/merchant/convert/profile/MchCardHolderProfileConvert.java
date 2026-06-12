package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchCardHolderProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchCardHolderProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchCardHolderProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 持卡人信息转换
///
@Mapper
public interface MchCardHolderProfileConvert {
    MchCardHolderProfileConvert CONVERT = Mappers.getMapper(MchCardHolderProfileConvert.class);

    MchCardHolderProfileResult toResult(MchCardHolderProfile entity);

    MchCardHolderProfile toEntity(MchCardHolderProfileParam param);
    
    void copy(MchCardHolderProfileParam param, @MappingTarget MchCardHolderProfile entity);
}