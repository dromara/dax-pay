package org.dromara.daxpay.service.convert.constant;

import org.dromara.daxpay.service.entity.constant.MethodConst;
import org.dromara.daxpay.service.result.constant.MethodConstResult;
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

    MethodConstResult toResult(MethodConst source);
}
