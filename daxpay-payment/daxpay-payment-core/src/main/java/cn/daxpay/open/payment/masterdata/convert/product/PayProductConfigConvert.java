package cn.daxpay.open.payment.masterdata.convert.product;

import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.payment.masterdata.result.product.PayProductConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付产品配置转换
///
@Mapper
public interface PayProductConfigConvert {

    PayProductConfigConvert CONVERT = Mappers.getMapper(PayProductConfigConvert.class);

    PayProductConfigResult toResult(PayProductConfig entity);
}
