package cn.daxpay.open.payment.merchant.convert.wxverify;

import cn.daxpay.open.payment.merchant.entity.wxverify.WxDomainVerify;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信域名验证文件转换
///
@Mapper
public interface WxDomainVerifyConvert {
    WxDomainVerifyConvert CONVERT = Mappers.getMapper(WxDomainVerifyConvert.class);

    WxDomainVerifyResult toResult(WxDomainVerify entity);

    WxDomainVerify toEntity(WxDomainVerifyParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WxDomainVerifyParam param, @MappingTarget WxDomainVerify entity);
}
