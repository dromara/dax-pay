package cn.daxpay.open.plugin.easypay.convert;

import cn.daxpay.open.plugin.easypay.entity.EasyPayConfig;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayConfigParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易支付场景配置转换
///
@Mapper
public interface EasyPayConfigConvert {
    EasyPayConfigConvert CONVERT = Mappers.getMapper(EasyPayConfigConvert.class);

    EasyPayConfigResult toResult(EasyPayConfig entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(EasyPayConfigParam param, @MappingTarget EasyPayConfig entity);
}
