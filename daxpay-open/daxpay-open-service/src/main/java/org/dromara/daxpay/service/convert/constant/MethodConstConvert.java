package org.dromara.daxpay.service.convert.constant;

import org.dromara.daxpay.service.entity.constant.PayMethodConst;
import org.dromara.daxpay.service.result.constant.PayMethodConstResult;
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
