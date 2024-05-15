package cn.bootx.platform.notice.core.mail.convert;

import cn.bootx.platform.notice.param.mail.MailConfigParam;
import cn.bootx.platform.notice.core.mail.entity.MailConfig;
import cn.bootx.platform.notice.dto.mail.MailConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 钉钉相关类转换
 *
 * @author xxm
 * @since 2021/8/5
 */
@Mapper
public interface MailConvert {

    MailConvert CONVERT = Mappers.getMapper(MailConvert.class);

    MailConfig convert(MailConfigDto in);

    MailConfig convert(MailConfigParam in);

    MailConfigDto convert(MailConfig in);

}
