package cn.daxpay.open.payment.wx.convert;

import cn.daxpay.open.payment.wx.entity.WxChannelAppCapability;
import cn.daxpay.open.payment.wx.param.WxChannelAppCapabilityParam;
import cn.daxpay.open.payment.wx.result.WxChannelAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道商户微信应用能力绑定转换
///
@Mapper
public interface WxChannelAppCapabilityConvert {

    WxChannelAppCapabilityConvert CONVERT = Mappers.getMapper(WxChannelAppCapabilityConvert.class);

    /// 转换为返回对象
    WxChannelAppCapabilityResult toResult(WxChannelAppCapability entity);

    /// 转换为实体
    WxChannelAppCapability toEntity(WxChannelAppCapabilityParam param);
}
