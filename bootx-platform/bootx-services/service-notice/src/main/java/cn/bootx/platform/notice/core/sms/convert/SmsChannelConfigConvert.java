package cn.bootx.platform.notice.core.sms.convert;

import cn.bootx.platform.notice.core.sms.entity.SmsChannelConfig;
import cn.bootx.platform.notice.dto.sms.SmsChannelConfigDto;
import cn.bootx.platform.notice.param.sms.SmsChannelConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 短信渠道配置
 * @author xxm
 * @since 2023-08-03
 */
@Mapper
public interface SmsChannelConfigConvert {
    SmsChannelConfigConvert CONVERT = Mappers.getMapper(SmsChannelConfigConvert.class);

    SmsChannelConfig convert(SmsChannelConfigParam in);

    SmsChannelConfigDto convert(SmsChannelConfig in);

}
