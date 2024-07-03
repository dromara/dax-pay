package cn.bootx.platform.iam.convert.permission;

import cn.bootx.platform.iam.entity.permission.PermMenu;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限转换
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface PermMenuConvert {

    PermMenuConvert CONVERT = Mappers.getMapper(PermMenuConvert.class);

    PermMenu convert(PermMenuParam in);

    PermMenuResult convert(PermMenu in);

}
