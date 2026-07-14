package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformWebsiteConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformWebsiteConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformWebsiteConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台站点配置转换
///
@Mapper
public interface PlatformWebsiteConfigConvert {
    PlatformWebsiteConfigConvert CONVERT = Mappers.getMapper(PlatformWebsiteConfigConvert.class);

    PlatformWebsiteConfigResult toResult(PlatformWebsiteConfig data);

    PlatformWebsiteConfig convert(PlatformWebsiteConfigParam param);

    /// 站点字段允许清空(传 null 覆盖原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void copy(PlatformWebsiteConfigParam param, @MappingTarget PlatformWebsiteConfig data);
}
