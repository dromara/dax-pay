package org.dromara.daxpay.payment.pay.convert.product;

import org.dromara.daxpay.payment.pay.entity.masterdata.product.PayProduct;
import org.dromara.daxpay.payment.pay.result.masterdata.product.PayProductResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品转换
///
@Mapper
public interface PayProductConvert {
    PayProductConvert CONVERT = Mappers.getMapper(PayProductConvert.class);

    PayProductResult toResult(PayProduct entity);
}
