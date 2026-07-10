package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformUrlConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformUrlConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台端点配置转换
///
@Mapper
public interface PlatformUrlConfigConvert {
    PlatformUrlConfigConvert CONVERT = Mappers.getMapper(PlatformUrlConfigConvert.class);

    PlatformUrlConfigResult toUrlResult(PlatformUrlConfig data);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformUrlConfigParam param, @MappingTarget PlatformUrlConfig data);
}
