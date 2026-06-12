package org.dromara.daxpay.payment.merchant.convert.profile;

import org.dromara.daxpay.payment.merchant.entity.profile.MchShopProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchShopProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchShopProfileResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户经营场所信息转换
///
@Mapper
public interface MchShopProfileConvert {
    MchShopProfileConvert CONVERT = Mappers.getMapper(MchShopProfileConvert.class);

    MchShopProfileResult toResult(MchShopProfile entity);

    MchShopProfile toEntity(MchShopProfileParam param);
    
    void copy(MchShopProfileParam param, @MappingTarget MchShopProfile entity);
}