package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformOssConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformOssConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台OSS配置转换
///
@Mapper
public interface PlatformOssConfigConvert {
    PlatformOssConfigConvert CONVERT = Mappers.getMapper(PlatformOssConfigConvert.class);

    PlatformOssConfigResult toOssResult(PlatformOssConfig data);

    PlatformOssConfig convert(PlatformOssConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformOssConfigParam param, @MappingTarget PlatformOssConfig data);
}
