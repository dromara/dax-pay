package cn.daxpay.multi.service.convert.constant;

import cn.daxpay.multi.service.entity.constant.MethodConst;
import cn.daxpay.multi.service.result.constant.MethodConstResult;
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
