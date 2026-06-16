package org.dromara.daxpay.payment.masterdata.constants.product.convert;

import org.dromara.daxpay.payment.masterdata.constants.product.entity.PayProduct;
import org.dromara.daxpay.payment.masterdata.constants.product.result.PayProductResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品转换
///
@Mapper
public interface PayProductConvert {
    PayProductConvert CONVERT = Mappers.getMapper(PayProductConvert.class);

    PayProductResult toResult(PayProduct entity);
}
