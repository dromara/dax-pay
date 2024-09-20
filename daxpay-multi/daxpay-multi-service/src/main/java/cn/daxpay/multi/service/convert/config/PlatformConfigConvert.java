package cn.daxpay.multi.service.convert.config;

import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.result.config.PlatformConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/9/19
 */
@Mapper
public interface PlatformConfigConvert {
    PlatformConfigConvert CONVERT = Mappers.getMapper(PlatformConfigConvert.class);

    PlatformConfigResult toResult(PlatformConfig entity);
}
