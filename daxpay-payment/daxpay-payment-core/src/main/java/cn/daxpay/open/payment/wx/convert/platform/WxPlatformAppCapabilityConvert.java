package cn.daxpay.open.payment.wx.convert.platform;

import cn.daxpay.open.payment.wx.entity.platform.WxPlatformAppCapability;
import cn.daxpay.open.payment.wx.param.platform.WxPlatformAppCapabilityParam;
import cn.daxpay.open.payment.wx.result.platform.WxPlatformAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 平台微信应用默认能力绑定转换
///
@Mapper
public interface WxPlatformAppCapabilityConvert {

    WxPlatformAppCapabilityConvert CONVERT = Mappers.getMapper(WxPlatformAppCapabilityConvert.class);

    /// 转换为返回对象
    WxPlatformAppCapabilityResult toResult(WxPlatformAppCapability entity);

    /// 转换为实体
    WxPlatformAppCapability toEntity(WxPlatformAppCapabilityParam param);
}
