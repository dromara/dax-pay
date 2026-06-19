package cn.daxpay.open.platform.iam.convert.permission;

import cn.daxpay.open.platform.iam.entity.permission.PermCodeData;
import cn.daxpay.open.platform.iam.param.permission.resource.PermCodeParam;
import cn.daxpay.open.platform.iam.result.permission.resource.PermCodeResult;
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

