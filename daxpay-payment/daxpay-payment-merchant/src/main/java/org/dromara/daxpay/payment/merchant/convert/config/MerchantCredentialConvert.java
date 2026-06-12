package org.dromara.daxpay.payment.merchant.convert.config;

import org.dromara.daxpay.payment.merchant.entity.config.MerchantCredential;
import org.dromara.daxpay.payment.merchant.param.config.MerchantCredentialParam;
import org.dromara.daxpay.payment.merchant.result.config.MerchantCredentialResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户API配置转换
///
@Mapper
public interface MerchantCredentialConvert {
    MerchantCredentialConvert CONVERT = Mappers.getMapper(MerchantCredentialConvert.class);

    MerchantCredential toEntity(MerchantCredentialParam param);

    MerchantCredentialResult toResult(MerchantCredential entity);

    void copy(MerchantCredentialParam param, @MappingTarget MerchantCredential entity);
}
