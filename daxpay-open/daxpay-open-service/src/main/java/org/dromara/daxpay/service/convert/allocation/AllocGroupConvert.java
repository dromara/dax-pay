package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroup;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupParam;
import org.dromara.daxpay.service.result.allocation.receiver.AllocGroupVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocGroupConvert {
    AllocGroupConvert CONVERT = Mappers.getMapper(AllocGroupConvert.class);

    AllocGroupVo toVo(AllocGroup in);

    AllocGroup toEntity(AllocGroupParam in);
}
