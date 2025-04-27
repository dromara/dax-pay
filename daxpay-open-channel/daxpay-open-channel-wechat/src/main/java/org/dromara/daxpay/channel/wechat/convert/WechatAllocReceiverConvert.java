package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.allocation.WechatAllocReceiver;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverParam;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/1/26
 */
@Mapper
public interface WechatAllocReceiverConvert {
    WechatAllocReceiverConvert CONVERT = Mappers.getMapper(WechatAllocReceiverConvert.class);

    WechatAllocReceiver copy(WechatAllocReceiver wechatAllocReceiver);

    WechatAllocReceiverResult toResult(WechatAllocReceiver wechatAllocReceiver);

    WechatAllocReceiver toEntity(WechatAllocReceiverParam param);
}
