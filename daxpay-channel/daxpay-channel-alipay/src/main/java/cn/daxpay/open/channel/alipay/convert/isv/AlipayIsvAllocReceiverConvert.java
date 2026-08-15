package cn.daxpay.open.channel.alipay.convert.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAllocReceiver;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商分账接收方转换
///
@Mapper
public interface AlipayIsvAllocReceiverConvert {

    AlipayIsvAllocReceiverConvert CONVERT = Mappers.getMapper(AlipayIsvAllocReceiverConvert.class);

    AlipayIsvAllocReceiverResult toResult(AlipayIsvAllocReceiver entity);
}
