package cn.bootx.platform.baseapi.core.template.convert;

import cn.bootx.platform.baseapi.core.template.entity.GeneralTemplate;
import cn.bootx.platform.baseapi.dto.template.GeneralTemplateDto;
import cn.bootx.platform.baseapi.param.template.GeneralTemplateParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@Mapper
public interface GeneralTemplateConvert {
    GeneralTemplateConvert CONVERT = Mappers.getMapper(GeneralTemplateConvert.class);

    GeneralTemplate convert(GeneralTemplateParam in);

    GeneralTemplateDto convert(GeneralTemplate in);

}
