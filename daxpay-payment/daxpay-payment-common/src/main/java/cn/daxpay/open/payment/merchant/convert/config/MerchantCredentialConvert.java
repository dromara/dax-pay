package cn.daxpay.open.payment.merchant.convert.config;

import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.factory.Mappers;

/// # 商户API配置转换
///
@Mapper
public interface MerchantCredentialConvert {
    MerchantCredentialConvert CONVERT = Mappers.getMapper(MerchantCredentialConvert.class);

    MerchantCredential toEntity(MerchantCredentialParam param);

    MerchantCredentialResult toResult(MerchantCredential entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MerchantCredentialParam param, @MappingTarget MerchantCredential entity);
}
