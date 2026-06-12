package org.dromara.daxpay.platform.iam.convert.permission;

import org.dromara.daxpay.platform.iam.entity.permission.PermMenu;
import org.dromara.daxpay.platform.iam.param.permission.resource.PermMenuParam;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermMenuResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 权限转换
///
@Mapper
public interface PermMenuConvert {

    PermMenuConvert CONVERT = Mappers.getMapper(PermMenuConvert.class);

    PermMenu convert(PermMenuParam in);

    PermMenuResult convert(PermMenu in);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void copy(PermMenuParam param, @MappingTarget PermMenu permMenu);
}
