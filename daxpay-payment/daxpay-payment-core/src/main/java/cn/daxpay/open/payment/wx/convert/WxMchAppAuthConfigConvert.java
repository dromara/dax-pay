package cn.daxpay.open.payment.wx.convert;

import cn.daxpay.open.payment.wx.entity.WxMchAppAuthConfig;
import cn.daxpay.open.payment.wx.param.WxMchAppAuthConfigParam;
import cn.daxpay.open.payment.wx.result.WxMchAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户微信应用授权认证配置转换
///
@Mapper
public interface WxMchAppAuthConfigConvert {

    WxMchAppAuthConfigConvert CONVERT = Mappers.getMapper(WxMchAppAuthConfigConvert.class);

    /// 转换为返回对象
    WxMchAppAuthConfigResult toResult(WxMchAppAuthConfig entity);

    /// 转换为实体
    WxMchAppAuthConfig toEntity(WxMchAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WxMchAppAuthConfigParam param, @MappingTarget WxMchAppAuthConfig entity);
}
