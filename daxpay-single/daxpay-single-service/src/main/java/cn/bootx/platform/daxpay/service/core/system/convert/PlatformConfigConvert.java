package cn.bootx.platform.daxpay.service.core.system.convert;

import cn.bootx.platform.daxpay.service.core.system.entity.PlatformConfig;
import cn.bootx.platform.daxpay.service.dto.system.PlatformConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/2
 */
@Mapper
public interface PlatformConfigConvert {
    PlatformConfigConvert CONVERT = Mappers.getMapper(PlatformConfigConvert.class);

    PlatformConfigDto convert(PlatformConfig in);
}
