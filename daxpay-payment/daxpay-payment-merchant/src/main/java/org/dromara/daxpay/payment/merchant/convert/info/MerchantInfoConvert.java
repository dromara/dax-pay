package org.dromara.daxpay.payment.merchant.convert.info;

import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.merchant.param.info.MerchantInfoParam;
import org.dromara.daxpay.payment.merchant.param.info.MerchantRegisterParam;
import org.dromara.daxpay.payment.merchant.result.info.MerchantInfoResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户转换
///
@Mapper
public interface MerchantInfoConvert {
    MerchantInfoConvert CONVERT = Mappers.getMapper(MerchantInfoConvert.class);

    MerchantInfo toEntity(MerchantInfoParam param);

    MerchantInfoResult toResult(MerchantInfo entity);

    MerchantInfo toEntity(MerchantRegisterParam param);

    void copy(MerchantRegisterParam param, @MappingTarget UserInfoParam userInfoParam);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MerchantInfoParam param, @MappingTarget MerchantInfo entity);
}
