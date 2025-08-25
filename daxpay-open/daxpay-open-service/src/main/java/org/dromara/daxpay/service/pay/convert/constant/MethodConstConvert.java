package org.dromara.daxpay.service.pay.convert.constant;

import org.dromara.daxpay.service.pay.entity.constant.PayMethodConst;
import org.dromara.daxpay.service.pay.result.constant.PayMethodConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface MethodConstConvert {
    MethodConstConvert CONVERT = Mappers.getMapper(MethodConstConvert.class);

    PayMethodConstResult toResult(PayMethodConst source);
}
