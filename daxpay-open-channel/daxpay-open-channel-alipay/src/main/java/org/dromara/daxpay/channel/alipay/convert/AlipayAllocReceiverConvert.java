package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.allocation.AlipayAllocReceiver;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverParam;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/1/26
 */
@Mapper
public interface AlipayAllocReceiverConvert {
    AlipayAllocReceiverConvert CONVERT = Mappers.getMapper(AlipayAllocReceiverConvert.class);

    AlipayAllocReceiver copy(AlipayAllocReceiver aliAllocReceiver);

    AlipayAllocReceiver toEntity(AlipayAllocReceiverParam alipayAllocReceiverParam);

    AlipayAllocReceiverResult toResult(AlipayAllocReceiver aliAllocReceiver);
}
