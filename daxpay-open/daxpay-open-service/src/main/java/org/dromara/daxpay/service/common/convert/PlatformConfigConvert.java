package org.dromara.daxpay.service.common.convert;

import org.dromara.daxpay.service.common.entity.config.PlatformBasicConfig;
import org.dromara.daxpay.service.common.entity.config.PlatformUrlConfig;
import org.dromara.daxpay.service.common.entity.config.PlatformWebsiteConfig;
import org.dromara.daxpay.service.common.result.config.PlatformBasicConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformUrlConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformWebsiteConfigResult;
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

    PlatformBasicConfigResult toResult(PlatformBasicConfig entity);

    PlatformUrlConfigResult toResult(PlatformUrlConfig platformUrlConfig);

    PlatformWebsiteConfigResult toResult(PlatformWebsiteConfig platformWebsiteConfig);

}
