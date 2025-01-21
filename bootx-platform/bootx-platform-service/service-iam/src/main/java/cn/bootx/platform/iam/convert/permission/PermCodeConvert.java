package cn.bootx.platform.iam.convert.permission;

import cn.bootx.platform.iam.entity.permission.PermCode;
import cn.bootx.platform.iam.param.permission.PermCodeParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限码转换
 * @author xxm
 * @since 2024/7/3
 */
@Mapper
public interface PermCodeConvert {
    PermCodeConvert CONVERT = Mappers.getMapper(PermCodeConvert.class);

    PermCodeResult convert(PermCode in);

    PermCode convert(PermCodeParam in);
}
