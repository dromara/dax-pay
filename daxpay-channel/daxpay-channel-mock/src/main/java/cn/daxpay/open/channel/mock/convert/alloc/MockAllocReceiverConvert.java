package cn.daxpay.open.channel.mock.convert.alloc;

import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import cn.daxpay.open.channel.mock.result.alloc.MockAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # Mock 通道分账接收方转换
///
@Mapper
public interface MockAllocReceiverConvert {

    MockAllocReceiverConvert CONVERT = Mappers.getMapper(MockAllocReceiverConvert.class);

    MockAllocReceiverResult toResult(MockAllocReceiver entity);
}
