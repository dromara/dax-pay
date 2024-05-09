package cn.bootx.platform.notice.core.template.convert;

import cn.bootx.platform.notice.param.template.MessageTemplateParam;
import cn.bootx.platform.notice.core.template.entity.MessageTemplate;
import cn.bootx.platform.notice.dto.template.MessageTemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 消息模板转换
 *
 * @author xxm
 * @since 2021/8/10
 */
@Mapper
public interface MessageTemplateConvert {

    MessageTemplateConvert CONVERT = Mappers.getMapper(MessageTemplateConvert.class);

    MessageTemplateDto convert(MessageTemplate in);

    MessageTemplate convert(MessageTemplateDto in);

    MessageTemplate convert(MessageTemplateParam in);

}
