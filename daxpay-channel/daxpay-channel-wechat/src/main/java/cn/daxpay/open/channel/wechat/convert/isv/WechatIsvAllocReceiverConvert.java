package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAllocReceiver;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信服务商分账接收方转换
///
@Mapper
public interface WechatIsvAllocReceiverConvert {

    WechatIsvAllocReceiverConvert CONVERT = Mappers.getMapper(WechatIsvAllocReceiverConvert.class);

    WechatIsvAllocReceiverResult toResult(WechatIsvAllocReceiver entity);
}
