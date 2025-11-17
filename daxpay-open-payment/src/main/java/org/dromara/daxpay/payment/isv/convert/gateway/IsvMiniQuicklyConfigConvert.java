package org.dromara.daxpay.payment.isv.convert.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvMiniQuicklyConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvMiniQuicklyConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvMiniQuicklyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * 小程序快捷支付配置转换
 * @author xxm
 * @since 2025/10/10
 */
@Mapper
public interface IsvMiniQuicklyConfigConvert {

    IsvMiniQuicklyConfigConvert CONVERT = Mappers.getMapper(IsvMiniQuicklyConfigConvert.class);

    /**
     * 参数转实体
     */
    IsvMiniQuicklyConfig toEntity(IsvMiniQuicklyConfigParam param);

    /**
     * 实体转结果
     */
    IsvMiniQuicklyConfigResult toResult(IsvMiniQuicklyConfig entity);

    /**
     * 复制属性
     */
    void copy(IsvMiniQuicklyConfigParam param, @MappingTarget IsvMiniQuicklyConfig entity);
}
