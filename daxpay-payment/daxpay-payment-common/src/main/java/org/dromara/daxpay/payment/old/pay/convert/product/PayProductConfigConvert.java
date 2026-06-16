package org.dromara.daxpay.payment.old.pay.convert.product;

import org.dromara.daxpay.payment.old.pay.entity.masterdata.product.PayProductConfig;
import org.dromara.daxpay.payment.old.pay.result.masterdata.product.PayProductConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品配置转换
///
@Mapper
public interface PayProductConfigConvert {

    PayProductConfigConvert CONVERT = Mappers.getMapper(PayProductConfigConvert.class);

    PayProductConfigResult toResult(PayProductConfig entity);
}
