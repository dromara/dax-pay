package org.dromara.daxpay.platform.iam.convert.permission;

import org.dromara.daxpay.platform.iam.entity.permission.PermCodeData;
import org.dromara.daxpay.platform.iam.param.permission.resource.PermCodeParam;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermCodeResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 权限码转换
///
@Mapper
public interface PermCodeConvert {
    PermCodeConvert CONVERT = Mappers.getMapper(PermCodeConvert.class);

    PermCodeResult convert(PermCodeData in);

    PermCodeData convert(PermCodeParam in);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget PermCodeData permCode, PermCodeParam param);
}

