package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppCapability;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信服务商通道商户应用支付能力关联转换
///
/// MapStruct转换器,负责关联记录在实体与返回结果之间的转换。
///
@Mapper
public interface WechatIsvMchAppCapabilityConvert {

    WechatIsvMchAppCapabilityConvert CONVERT = Mappers.getMapper(WechatIsvMchAppCapabilityConvert.class);

    /// 转换为返回对象
    WechatIsvMchAppCapabilityResult toResult(WechatIsvMchAppCapability entity);
}
