package cn.bootx.platform.baseapi.convert.parameter;

import cn.bootx.platform.baseapi.entity.parameter.SystemParameter;
import cn.bootx.platform.baseapi.param.parameter.SystemParameterParam;
import cn.bootx.platform.baseapi.result.parameter.SystemParameterResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 系统参数和系统配置实体类转换
 *
 * @author xxm
 * @since 2021/10/25
 */
@Mapper
public interface SystemConvert {

    SystemConvert CONVERT = Mappers.getMapper(SystemConvert.class);

    SystemParameterResult convert(SystemParameter in);

    SystemParameter convert(SystemParameterParam in);

}
