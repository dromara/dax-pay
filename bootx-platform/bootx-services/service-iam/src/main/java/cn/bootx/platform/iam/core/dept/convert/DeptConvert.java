package cn.bootx.platform.iam.core.dept.convert;

import cn.bootx.platform.iam.param.dept.DeptParam;
import cn.bootx.platform.iam.core.dept.entity.Dept;
import cn.bootx.platform.iam.dto.dept.DeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 部门机构转换
 *
 * @author xxm
 * @since 2021/8/4
 */
@Mapper
public interface DeptConvert {

    DeptConvert CONVERT = Mappers.getMapper(DeptConvert.class);

    Dept convert(DeptDto in);

    Dept convert(DeptParam in);

    DeptDto convert(Dept in);

}
