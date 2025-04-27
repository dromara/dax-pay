package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.entity.allocation.AllocConfig;
import org.dromara.daxpay.service.param.allocation.AllocConfigParam;
import org.dromara.daxpay.service.result.allocation.AllocConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 分账配置
 * @author xxm
 * @since 2024/12/9
 */
@Mapper
public interface AllocConfigConvert {

    AllocConfigConvert CONVERT = Mappers.getMapper(AllocConfigConvert.class);

    AllocConfigResult toResult(AllocConfig in);

    AllocConfig toEntity(AllocConfigParam in);
}
