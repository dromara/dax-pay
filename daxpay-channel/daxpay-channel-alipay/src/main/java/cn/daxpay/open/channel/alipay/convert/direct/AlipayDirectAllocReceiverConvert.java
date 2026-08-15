package cn.daxpay.open.channel.alipay.convert.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAllocReceiver;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连分账接收方转换
///
@Mapper
public interface AlipayDirectAllocReceiverConvert {

    AlipayDirectAllocReceiverConvert CONVERT = Mappers.getMapper(AlipayDirectAllocReceiverConvert.class);

    AlipayDirectAllocReceiverResult toResult(AlipayDirectAllocReceiver entity);
}
