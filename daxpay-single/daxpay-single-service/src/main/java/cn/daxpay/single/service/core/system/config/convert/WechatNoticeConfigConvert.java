package cn.daxpay.single.service.core.system.config.convert;

import cn.daxpay.single.service.core.system.config.entity.WechatNoticeConfig;
import cn.daxpay.single.service.dto.system.config.WechatNoticeConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/2
 */
@Mapper
public interface WechatNoticeConfigConvert {
    WechatNoticeConfigConvert CONVERT = Mappers.getMapper(WechatNoticeConfigConvert.class);

    WechatNoticeConfigDto convert(WechatNoticeConfig in);
}
