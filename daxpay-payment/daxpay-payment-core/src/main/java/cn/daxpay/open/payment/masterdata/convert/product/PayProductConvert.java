package cn.daxpay.open.payment.masterdata.convert.product;

import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.payment.masterdata.result.product.PayProductResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品转换
///
@Mapper
public interface PayProductConvert {
    PayProductConvert CONVERT = Mappers.getMapper(PayProductConvert.class);

    PayProductResult toResult(PayProduct entity);
}
