package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.bo.allocation.AllocGroupReceiverResultBo;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroupReceiver;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupReceiverParam;
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

    AllocGroupReceiverResultBo convert(AllocGroupReceiver in);

    AllocGroupReceiver convert(AllocGroupReceiverParam in);
}
