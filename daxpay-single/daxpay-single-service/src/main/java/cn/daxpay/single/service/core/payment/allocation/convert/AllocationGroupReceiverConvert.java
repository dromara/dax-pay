package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.service.core.payment.allocation.entity.AllocationGroupReceiver;
import cn.daxpay.single.service.dto.allocation.AllocationGroupReceiverResult;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupReceiverParam;
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

    AllocationGroupReceiverResult convert(AllocationGroupReceiver in);

    AllocationGroupReceiver convert(AllocationGroupReceiverParam in);
}
