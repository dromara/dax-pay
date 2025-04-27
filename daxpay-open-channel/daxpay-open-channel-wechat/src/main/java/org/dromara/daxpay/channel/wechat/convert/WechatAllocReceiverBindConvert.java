package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.allocation.WechatAllocReceiverBind;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindParam;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverBindResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/1/27
 */
@Mapper
public interface WechatAllocReceiverBindConvert {
    WechatAllocReceiverBindConvert CONVERT = Mappers.getMapper(WechatAllocReceiverBindConvert.class);

    WechatAllocReceiverBindResult toResult(WechatAllocReceiverBind wxReceiver);

    WechatAllocReceiverBind toEntity(WechatAllocReceiverBindParam wxReceiver);
}
