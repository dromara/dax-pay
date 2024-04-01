package cn.bootx.platform.daxpay.service.core.payment.allocation.convert;

import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationGroupReceiver;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverDto;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationGroupReceiverParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocationGroupReceiverConvert {
    AllocationGroupReceiverConvert CONVERT = Mappers.getMapper(AllocationGroupReceiverConvert.class);

    AllocationGroupReceiverDto convert(AllocationGroupReceiver in);

    AllocationGroupReceiver convert(AllocationGroupReceiverParam in);
}
