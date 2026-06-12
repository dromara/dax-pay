package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbShopProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbShopProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件门店信息转换
///
@Mapper
public interface OnbShopProfileConvert {
    OnbShopProfileConvert CONVERT = Mappers.getMapper(OnbShopProfileConvert.class);

    OnbShopProfile toEntity(OnbShopProfileBo param);

    OnbShopProfileBo toResult(OnbShopProfile entity);
    
    void copy(OnbShopProfileBo param, @MappingTarget OnbShopProfile entity);
}
