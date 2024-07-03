package cn.bootx.platform.iam.convert.role;

import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConvert {

    RoleConvert CONVERT = Mappers.getMapper(RoleConvert.class);

    RoleResult convert(Role in);

    Role convert(RoleParam in);

}
