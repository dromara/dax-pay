package cn.daxpay.open.platform.system.mobile.config.convert;

import cn.daxpay.open.platform.system.mobile.config.AlipayMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.DyMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.WxMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.param.AlipayMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.DyMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.WxMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.result.AlipayMiniAppConfigResult;
import cn.daxpay.open.platform.system.mobile.config.result.DyMiniAppConfigResult;
import cn.daxpay.open.platform.system.mobile.config.result.WxMiniAppConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 移动端各平台应用配置转换
///
/// Param → Config 使用 IGNORE, 空敏感字段不覆盖目标(merge 时先 copy 旧值再 overlay)。
@Mapper
public interface MobileAppConfigConvert {

    MobileAppConfigConvert CONVERT = Mappers.getMapper(MobileAppConfigConvert.class);

    WxMiniAppConfig toConfig(WxMiniAppConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WxMiniAppConfigParam param, @MappingTarget WxMiniAppConfig config);

    WxMiniAppConfigResult toResult(WxMiniAppConfig config);

    AlipayMiniAppConfig toConfig(AlipayMiniAppConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayMiniAppConfigParam param, @MappingTarget AlipayMiniAppConfig config);

    AlipayMiniAppConfigResult toResult(AlipayMiniAppConfig config);

    DyMiniAppConfig toConfig(DyMiniAppConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DyMiniAppConfigParam param, @MappingTarget DyMiniAppConfig config);

    DyMiniAppConfigResult toResult(DyMiniAppConfig config);
}
