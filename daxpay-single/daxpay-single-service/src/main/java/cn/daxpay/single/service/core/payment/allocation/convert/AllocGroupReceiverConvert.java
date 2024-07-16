package cn.daxpay.single.service.core.payment.allocation.convert;

import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroupReceiver;
import cn.daxpay.single.service.dto.allocation.AllocGroupReceiverResult;
import cn.daxpay.single.service.param.allocation.group.AllocGroupReceiverParam;
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
