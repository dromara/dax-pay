package cn.daxpay.open.platform.system.convert.config.auth;

import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.param.config.auth.PlatformDouyinH5AuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformDouyinH5AuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台抖音开放平台 H5 应用认证配置转换
///
/// copy 使用 IGNORE 策略, 前端未传的敏感字段(null)不会覆盖数据库原值,
/// 与"前端 diffForm + 后端 NOT_NULL"双重保护配合, 避免误清空。
///
@Mapper
public interface PlatformDouyinH5AuthConfigConvert {

    PlatformDouyinH5AuthConfigConvert CONVERT = Mappers.getMapper(PlatformDouyinH5AuthConfigConvert.class);

    PlatformDouyinH5AuthConfigResult toResult(PlatformDouyinH5AuthConfig data);

    PlatformDouyinH5AuthConfig convert(PlatformDouyinH5AuthConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformDouyinH5AuthConfigParam param, @MappingTarget PlatformDouyinH5AuthConfig data);
}
