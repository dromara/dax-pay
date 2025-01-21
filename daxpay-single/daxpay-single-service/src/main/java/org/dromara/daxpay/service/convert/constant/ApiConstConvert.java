package org.dromara.daxpay.service.convert.constant;

import org.dromara.daxpay.service.entity.constant.ApiConst;
import org.dromara.daxpay.service.result.constant.ApiConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface ApiConstConvert {
    ApiConstConvert CONVERT = Mappers.getMapper(ApiConstConvert.class);

    ApiConstResult toResult(ApiConst source);
}
