package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.core.param.allocation.receiver.AllocReceiverAddParam;
import org.dromara.daxpay.core.result.allocation.receiver.AllocReceiverResult;
import org.dromara.daxpay.service.result.allocation.receiver.AllocReceiverVo;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/3/28
 */
@Mapper
public interface AllocReceiverConvert {
    AllocReceiverConvert CONVERT = Mappers.getMapper(AllocReceiverConvert.class);

    AllocReceiver convert(AllocReceiverAddParam in);

    AllocReceiverVo toBo(AllocReceiver in);

    AllocReceiverResult toResult(AllocReceiver in);

    List<AllocReceiverResult.Receiver> toList(List<AllocReceiver> in);
}
