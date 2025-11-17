package org.dromara.daxpay.payment.merchant.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvMiniQuicklyConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.MiniQuicklyConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.MiniQuicklyConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.MiniQuicklyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 小程序快捷支付配置转换
 * @author xxm
 * @since 2025/10/10
 */
@Mapper
public interface MiniQuicklyConfigConvert {
    MiniQuicklyConfigConvert CONVERT = Mappers.getMapper(MiniQuicklyConfigConvert.class);

    MiniQuicklyConfig toEntity(MiniQuicklyConfigParam param);

    MiniQuicklyConfigResult toResult(MiniQuicklyConfig entity);

    void copy(MiniQuicklyConfigParam param, @MappingTarget MiniQuicklyConfig target);

    void copy(IsvMiniQuicklyConfig isvQuicklyConfig, @MappingTarget MiniQuicklyConfig quicklyConfig);
}
