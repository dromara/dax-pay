package org.dromara.daxpay.service.pay.convert.constant;

import org.dromara.daxpay.service.pay.entity.constant.ApiConst;
import org.dromara.daxpay.service.pay.result.constant.ApiConstResult;
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
