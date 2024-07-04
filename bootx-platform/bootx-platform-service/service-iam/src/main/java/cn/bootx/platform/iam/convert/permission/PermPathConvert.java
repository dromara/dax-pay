package cn.bootx.platform.iam.convert.permission;

import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限转换
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface PermPathConvert {

    PermPathConvert CONVERT = Mappers.getMapper(PermPathConvert.class);

    PermPathResult convert(PermPath in);

}
