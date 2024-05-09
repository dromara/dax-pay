package cn.bootx.platform.iam.core.role.convert;

import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.core.role.entity.Role;
import cn.bootx.platform.iam.dto.role.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConvert {

    RoleConvert CONVERT = Mappers.getMapper(RoleConvert.class);

    RoleDto convert(Role in);

    Role convert(RoleParam in);

}
