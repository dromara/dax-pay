package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroup;
import cn.daxpay.single.service.dto.allocation.AllocGroupDto;
import cn.daxpay.single.service.param.allocation.group.AllocGroupParam;
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

    AllocGroupDto convert(AllocGroup in);

    AllocGroup convert(AllocGroupParam in);
}
