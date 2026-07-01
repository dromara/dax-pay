package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppCapability;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信服务商应用支付能力关联转换
///
/// MapStruct转换器，负责关联记录在实体与返回结果之间的转换。
///
@Mapper
public interface WechatIsvAppCapabilityConvert {

    WechatIsvAppCapabilityConvert CONVERT = Mappers.getMapper(WechatIsvAppCapabilityConvert.class);

    /// 转换为返回对象
    WechatIsvAppCapabilityResult toResult(WechatIsvAppCapability entity);
}
