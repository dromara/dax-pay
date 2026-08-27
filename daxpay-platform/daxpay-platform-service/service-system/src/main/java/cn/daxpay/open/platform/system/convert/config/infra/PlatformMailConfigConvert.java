package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMailConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMailConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台邮件发件箱配置转换
///
@Mapper
public interface PlatformMailConfigConvert {
    PlatformMailConfigConvert CONVERT = Mappers.getMapper(PlatformMailConfigConvert.class);

    PlatformMailConfigResult toMailResult(PlatformMailConfig data);

    PlatformMailConfig convert(PlatformMailConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformMailConfigParam param, @MappingTarget PlatformMailConfig data);
}
