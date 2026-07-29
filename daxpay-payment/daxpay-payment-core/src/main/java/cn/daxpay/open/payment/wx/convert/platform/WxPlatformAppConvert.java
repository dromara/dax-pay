package cn.daxpay.open.payment.wx.convert.platform;

import cn.daxpay.open.payment.wx.entity.platform.WxPlatformApp;
import cn.daxpay.open.payment.wx.param.platform.WxPlatformAppParam;
import cn.daxpay.open.payment.wx.result.platform.WxPlatformAppResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台微信应用转换
///
@Mapper
public interface WxPlatformAppConvert {

    WxPlatformAppConvert CONVERT = Mappers.getMapper(WxPlatformAppConvert.class);

    /// 转换为返回对象
    WxPlatformAppResult toResult(WxPlatformApp entity);

    /// 转换为实体
    WxPlatformApp toEntity(WxPlatformAppParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WxPlatformAppParam param, @MappingTarget WxPlatformApp entity);
}
