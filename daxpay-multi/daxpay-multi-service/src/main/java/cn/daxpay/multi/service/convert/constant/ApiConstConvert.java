package cn.daxpay.multi.service.convert.constant;

import cn.daxpay.multi.service.entity.constant.ApiConst;
import cn.daxpay.multi.service.result.constant.ApiConstResult;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
public interface ApiConstConvert {
    ApiConstConvert CONVERT = Mappers.getMapper(ApiConstConvert.class);

    ApiConstResult toResult(ApiConst source);
}
