package org.dromara.daxpay.payment.merchant.convert.config;

import org.dromara.daxpay.payment.merchant.entity.config.MchProductConfig;
import org.dromara.daxpay.payment.merchant.result.config.MchProductConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 商户产品配置转换
///
@Mapper
public interface MchProductConfigConvert {

    MchProductConfigConvert CONVERT = Mappers.getMapper(MchProductConfigConvert.class);

    MchProductConfigResult toResult(MchProductConfig entity);
}
