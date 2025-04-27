package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.allocation.AlipayAllocReceiverBind;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindParam;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverBindResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/1/27
 */
@Mapper
public interface AlipayAllocReceiverBindConvert {
    AlipayAllocReceiverBindConvert CONVERT = Mappers.getMapper(AlipayAllocReceiverBindConvert.class);

    AlipayAllocReceiverBindResult toResult(AlipayAllocReceiverBind alipayAllocReceiverBind);

    AlipayAllocReceiverBind toEntity(AlipayAllocReceiverBindParam param);
}
