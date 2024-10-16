package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroupReceiver;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupReceiverParam;
import org.dromara.daxpay.service.result.allocation.AllocGroupReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocGroupReceiverConvert {
    AllocGroupReceiverConvert CONVERT = Mappers.getMapper(AllocGroupReceiverConvert.class);

    AllocGroupReceiverResult convert(AllocGroupReceiver in);

    AllocGroupReceiver convert(AllocGroupReceiverParam in);
}
