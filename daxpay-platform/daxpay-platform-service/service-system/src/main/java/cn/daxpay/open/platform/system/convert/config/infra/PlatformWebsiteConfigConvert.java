package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformWebsiteConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformWebsiteConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformWebsiteConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 平台站点配置转换
///
@Mapper
public interface PlatformWebsiteConfigConvert {
    PlatformWebsiteConfigConvert CONVERT = Mappers.getMapper(PlatformWebsiteConfigConvert.class);

    PlatformWebsiteConfigResult toResult(PlatformWebsiteConfig data);

    PlatformWebsiteConfig convert(PlatformWebsiteConfigParam param);
}
