package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.param.payment.allocation.AllocReceiverAddParam;
import cn.daxpay.single.result.allocation.AllocReceiverResult;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationReceiver;
import cn.daxpay.single.service.dto.allocation.AllocationReceiverDto;
import cn.daxpay.single.service.param.allocation.receiver.AllocationReceiverParam;
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

    AllocReceiverResult toResult(AllocationReceiver in);

    AllocationReceiver convert(AllocationReceiverParam in);

    AllocationReceiver convert(AllocReceiverAddParam in);
}
