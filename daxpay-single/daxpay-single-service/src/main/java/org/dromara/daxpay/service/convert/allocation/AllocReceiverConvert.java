package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.core.param.allocation.AllocReceiverAddParam;
import org.dromara.daxpay.service.result.allocation.AllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/3/28
 */
@Mapper
public interface AllocReceiverConvert {
    AllocReceiverConvert CONVERT = Mappers.getMapper(AllocReceiverConvert.class);

    AllocReceiver convert(AllocReceiverAddParam in);

    AllocReceiverResult toResult(AllocReceiver in);
}
