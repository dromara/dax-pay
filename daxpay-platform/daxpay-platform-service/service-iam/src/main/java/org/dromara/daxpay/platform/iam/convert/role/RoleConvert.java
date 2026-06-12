package org.dromara.daxpay.platform.iam.convert.role;

import org.dromara.daxpay.platform.iam.entity.role.Role;
import org.dromara.daxpay.platform.iam.param.role.RoleParam;
import org.dromara.daxpay.platform.iam.result.role.RoleResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConvert {

    RoleConvert CONVERT = Mappers.getMapper(RoleConvert.class);

    RoleResult convert(Role in);

    Role convert(RoleParam in);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget Role role, RoleParam roleParam);
}
