package cn.bootx.platform.notice.core.site.convert;

import cn.bootx.platform.notice.core.site.entity.SiteMessage;
import cn.bootx.platform.notice.dto.site.SiteMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 站内信转换
 *
 * @author xxm
 * @since 2021/8/7
 */
@Mapper
public interface SiteMessageConvert {

    SiteMessageConvert CONVERT = Mappers.getMapper(SiteMessageConvert.class);

    SiteMessageDto convert(SiteMessage in);

}
