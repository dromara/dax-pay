package cn.daxpay.open.payment.wx.convert;

import cn.daxpay.open.payment.wx.entity.WxPlatformAppAuthConfig;
import cn.daxpay.open.payment.wx.param.WxPlatformAppAuthConfigParam;
import cn.daxpay.open.payment.wx.result.WxPlatformAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台微信应用授权认证配置转换
///
@Mapper
public interface WxPlatformAppAuthConfigConvert {

    WxPlatformAppAuthConfigConvert CONVERT = Mappers.getMapper(WxPlatformAppAuthConfigConvert.class);

    /// 转换为返回对象
    WxPlatformAppAuthConfigResult toResult(WxPlatformAppAuthConfig entity);

    /// 转换为实体
    WxPlatformAppAuthConfig toEntity(WxPlatformAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WxPlatformAppAuthConfigParam param, @MappingTarget WxPlatformAppAuthConfig entity);
}
