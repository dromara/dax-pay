package cn.bootx.platform.baseapi.core.dynamicform.convert;

import cn.bootx.platform.baseapi.dto.dynamicform.DynamicFormDto;
import cn.bootx.platform.baseapi.param.dynamicform.DynamicFormParam;
import cn.bootx.platform.baseapi.core.dynamicform.entity.DynamicForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@Mapper
public interface DynamicFormConvert {

    DynamicFormConvert CONVERT = Mappers.getMapper(DynamicFormConvert.class);

    DynamicForm convert(DynamicFormParam in);

    DynamicFormDto convert(DynamicForm in);

}
