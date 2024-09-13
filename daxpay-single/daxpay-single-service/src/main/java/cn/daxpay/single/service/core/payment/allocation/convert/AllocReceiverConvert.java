package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.core.param.payment.allocation.AllocReceiverAddParam;
import cn.daxpay.single.core.result.allocation.AllocReceiverResult;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import cn.daxpay.single.service.dto.allocation.AllocReceiverDto;
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

    AllocReceiverDto convert(AllocReceiver in);

    AllocReceiverResult toResult(AllocReceiver in);

    AllocReceiver convert(AllocReceiverAddParam in);
}
