package org.dromara.daxpay.service.merchant.convert.app;

import org.dromara.daxpay.service.merchant.entity.app.MchApp;
import org.dromara.daxpay.service.merchant.param.app.MchAppParam;
import org.dromara.daxpay.service.merchant.result.app.MchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/24
 */
@Mapper
public interface MchAppConvert {
    MchAppConvert CONVERT = Mappers.getMapper(MchAppConvert.class);

    MchAppResult toResult(MchApp entity);

    MchApp toEntity(MchAppParam param);
}
