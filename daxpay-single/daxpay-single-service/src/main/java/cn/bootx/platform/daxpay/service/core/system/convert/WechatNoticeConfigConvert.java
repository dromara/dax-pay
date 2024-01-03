package cn.bootx.platform.daxpay.service.core.system.convert;

import cn.bootx.platform.daxpay.service.core.system.entity.WechatNoticeConfig;
import cn.bootx.platform.daxpay.service.dto.system.WechatNoticeConfigDto;
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
