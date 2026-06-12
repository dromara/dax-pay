package org.dromara.daxpay.payment.merchant.convert.appinfo;

import org.dromara.daxpay.payment.merchant.entity.appinfo.MchAppInfo;
import org.dromara.daxpay.payment.merchant.param.appinfo.MchAppInfoParam;
import org.dromara.daxpay.payment.merchant.result.appinfo.MchAppInfoResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户应用信息转换
///
@Mapper
public interface MchAppInfoConvert {
    MchAppInfoConvert CONVERT = Mappers.getMapper(MchAppInfoConvert.class);

    MchAppInfoResult toResult(MchAppInfo entity);

    MchAppInfo toEntity(MchAppInfoParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MchAppInfoParam param, @MappingTarget MchAppInfo mchApp);
}
