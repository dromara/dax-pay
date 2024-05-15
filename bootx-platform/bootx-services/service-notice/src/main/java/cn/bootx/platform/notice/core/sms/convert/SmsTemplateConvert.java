package cn.bootx.platform.notice.core.sms.convert;

import cn.bootx.platform.notice.core.sms.entity.SmsTemplate;
import cn.bootx.platform.notice.dto.sms.SmsTemplateDto;
import cn.bootx.platform.notice.param.sms.SmsTemplateParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@Mapper
public interface SmsTemplateConvert {
    SmsTemplateConvert CONVERT = Mappers.getMapper(SmsTemplateConvert.class);

    SmsTemplate convert(SmsTemplateParam in);

    SmsTemplateDto convert(SmsTemplate in);

}
