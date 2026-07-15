package cn.daxpay.open.plugin.easypay.convert;

import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayCredentialParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayCredentialResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易支付凭证转换
///
@Mapper
public interface EasyPayCredentialConvert {
    EasyPayCredentialConvert CONVERT = Mappers.getMapper(EasyPayCredentialConvert.class);

    EasyPayCredentialResult toResult(EasyPayCredential entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(EasyPayCredentialParam param, @MappingTarget EasyPayCredential entity);
}
