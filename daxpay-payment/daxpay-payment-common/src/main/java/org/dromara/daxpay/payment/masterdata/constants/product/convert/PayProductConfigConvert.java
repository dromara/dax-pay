package org.dromara.daxpay.payment.masterdata.constants.product.convert;

import org.dromara.daxpay.payment.masterdata.constants.product.entity.PayProductConfig;
import org.dromara.daxpay.payment.masterdata.constants.product.result.PayProductConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品配置转换
///
@Mapper
public interface PayProductConfigConvert {

    PayProductConfigConvert CONVERT = Mappers.getMapper(PayProductConfigConvert.class);

    PayProductConfigResult toResult(PayProductConfig entity);
}
