package cn.bootx.platform.daxpay.service.core.payment.allocation.convert;

import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationReceiverDto;
import cn.bootx.platform.daxpay.service.param.allocation.group.AllocationReceiverParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/3/28
 */
@Mapper
public interface AllocationReceiverConvert {
    AllocationReceiverConvert CONVERT = Mappers.getMapper(AllocationReceiverConvert.class);

    AllocationReceiverDto convert(AllocationReceiver in);

    AllocationReceiver convert(AllocationReceiverParam in);
}
