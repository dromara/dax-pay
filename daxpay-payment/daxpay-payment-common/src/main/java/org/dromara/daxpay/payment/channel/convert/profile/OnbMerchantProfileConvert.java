package org.dromara.daxpay.payment.channel.convert.profile;

import org.dromara.daxpay.payment.channel.bo.profile.OnbBaseProfileBo;
import org.dromara.daxpay.payment.channel.entity.profile.OnbBaseProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 进件商户信息转换
///
@Mapper
public interface OnbMerchantProfileConvert {
    OnbMerchantProfileConvert CONVERT = Mappers.getMapper(OnbMerchantProfileConvert.class);

    OnbBaseProfile toEntity(OnbBaseProfileBo param);

    @Mapping(target = "businessContentName", ignore = true)
    OnbBaseProfileBo toResult(OnbBaseProfile entity);

    void copy(OnbBaseProfileBo param, @MappingTarget OnbBaseProfile entity);
}
