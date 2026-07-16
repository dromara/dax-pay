package cn.daxpay.open.platform.system.convert.config.infra;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformSensitiveWordConfig;
import cn.daxpay.open.platform.system.param.config.infra.PlatformSensitiveWordConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformSensitiveWordConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 平台敏感词策略转换
///
@Mapper
public interface PlatformSensitiveWordConfigConvert {
    PlatformSensitiveWordConfigConvert CONVERT = Mappers.getMapper(PlatformSensitiveWordConfigConvert.class);

    PlatformSensitiveWordConfigResult toResult(PlatformSensitiveWordConfig config);

    PlatformSensitiveWordConfig convert(PlatformSensitiveWordConfigParam param);
}
