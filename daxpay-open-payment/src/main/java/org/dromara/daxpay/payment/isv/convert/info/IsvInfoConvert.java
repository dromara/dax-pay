package org.dromara.daxpay.payment.isv.convert.info;

import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoParam;
import org.dromara.daxpay.payment.isv.result.info.IsvInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/10/29
 */
@Mapper
public interface IsvInfoConvert {
    IsvInfoConvert CONVERT = Mappers.getMapper(IsvInfoConvert.class);

    IsvInfo toEntity(IsvInfoParam param);

    IsvInfoResult toResult(IsvInfo entity);
}
