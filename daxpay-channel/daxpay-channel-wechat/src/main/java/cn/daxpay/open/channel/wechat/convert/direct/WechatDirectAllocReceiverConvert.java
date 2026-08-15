package cn.daxpay.open.channel.wechat.convert.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAllocReceiver;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信直连分账接收方转换
///
@Mapper
public interface WechatDirectAllocReceiverConvert {

    WechatDirectAllocReceiverConvert CONVERT = Mappers.getMapper(WechatDirectAllocReceiverConvert.class);

    WechatDirectAllocReceiverResult toResult(WechatDirectAllocReceiver entity);
}
