package cn.daxpay.open.channel.wechat.convert.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppCapability;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信直连商户应用支付能力关联转换
///
/// MapStruct转换器，负责关联记录在实体与返回结果之间的转换。
///
@Mapper
public interface WechatDirectAppCapabilityConvert {

    WechatDirectAppCapabilityConvert CONVERT = Mappers.getMapper(WechatDirectAppCapabilityConvert.class);

    /// 转换为返回对象
    WechatDirectAppCapabilityResult toResult(WechatDirectAppCapability entity);
}
