package org.dromara.daxpay.service.convert.merchant;

import org.dromara.daxpay.service.entity.merchant.MchApp;
import org.dromara.daxpay.service.param.merchant.MchAppParam;
import org.dromara.daxpay.service.result.merchant.MchAppResult;
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
