package cn.bootx.platform.starter.wechat.core.notice.convert;

import cn.bootx.platform.starter.wechat.param.notice.WeChatTemplateParam;
import cn.bootx.platform.starter.wechat.core.notice.entity.WeChatTemplate;
import cn.bootx.platform.starter.wechat.dto.notice.WeChatTemplateDto;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/7/17
 */
@Mapper
public interface WeChatTemplateConvert {

    WeChatTemplateConvert CONVERT = Mappers.getMapper(WeChatTemplateConvert.class);

    @Mapping(source = "title", target = "name")
    WeChatTemplate convert(WxMpTemplate in);

    WeChatTemplate convert(WeChatTemplateParam in);

    WeChatTemplateDto convert(WeChatTemplate in);

}
