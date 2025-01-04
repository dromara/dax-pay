package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.result.allocation.receiver.AllocGroupReceiverVo;
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

    AllocGroupReceiverVo toVo(AllocGroupReceiver in);

    AllocGroupReceiver toEntity(AllocGroupReceiverParam in);
}
