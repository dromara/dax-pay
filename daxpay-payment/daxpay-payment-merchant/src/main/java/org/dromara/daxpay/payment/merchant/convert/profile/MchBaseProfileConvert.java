package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchBaseProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchBaseProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBaseProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户基础资料转换
///
@Mapper
public interface MchBaseProfileConvert {
    MchBaseProfileConvert CONVERT = Mappers.getMapper(MchBaseProfileConvert.class);

    MchBaseProfileResult toResult(MchBaseProfile entity);

    MchBaseProfile toEntity(MchBaseProfileParam param);

    void copy(MchBaseProfileParam param, @MappingTarget MchBaseProfile entity);
}
