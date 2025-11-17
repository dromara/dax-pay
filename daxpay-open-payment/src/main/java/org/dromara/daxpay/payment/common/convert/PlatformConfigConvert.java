package org.dromara.daxpay.payment.common.convert;

import org.dromara.daxpay.payment.common.entity.config.*;
import org.dromara.daxpay.payment.common.param.config.PlatformOcrConfigParam;

import org.dromara.daxpay.payment.common.result.config.platform.*;
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

    PlatformIntegrationConfigResult toResult(PlatformIntegrationConfig platformIntegrationConfig);

}
