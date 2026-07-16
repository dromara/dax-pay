package cn.daxpay.open.platform.capability.sensitiveword.convert;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordParam;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 敏感词转换
///
@Mapper
public interface SystemSensitiveWordConvert {
    SystemSensitiveWordConvert CONVERT = Mappers.getMapper(SystemSensitiveWordConvert.class);

    SystemSensitiveWordResult toResult(SystemSensitiveWord entity);

    SystemSensitiveWord toEntity(SystemSensitiveWordParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(SystemSensitiveWordParam param, @MappingTarget SystemSensitiveWord entity);
}

