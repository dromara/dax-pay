package cn.bootx.platform.iam.core.scope.convert;

import cn.bootx.platform.iam.param.scope.DataRoleParam;
import cn.bootx.platform.iam.core.scope.entity.DataRole;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据范围转换类
 *
 * @author xxm
 * @since 2021/12/23
 */
@Mapper
public interface DataScopeConvert {

    DataScopeConvert CONVERT = Mappers.getMapper(DataScopeConvert.class);

    DataRole convert(DataRoleParam in);

    DataRoleDto convert(DataRole in);

}
