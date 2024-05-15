package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.service.core.payment.allocation.entity.AllocationGroup;
import cn.daxpay.single.service.dto.allocation.AllocationGroupDto;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocationGroupConvert {
    AllocationGroupConvert CONVERT = Mappers.getMapper(AllocationGroupConvert.class);

    AllocationGroupDto convert(AllocationGroup in);

    AllocationGroup convert(AllocationGroupParam in);
}
