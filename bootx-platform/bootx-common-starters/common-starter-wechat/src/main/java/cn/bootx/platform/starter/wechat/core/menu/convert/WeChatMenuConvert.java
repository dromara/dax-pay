package cn.bootx.platform.starter.wechat.core.menu.convert;

import cn.bootx.platform.starter.wechat.param.menu.WeChatMenuParam;
import cn.bootx.platform.starter.wechat.core.menu.domin.WeChatMenuInfo;
import cn.bootx.platform.starter.wechat.core.menu.entity.WeChatMenu;
import cn.bootx.platform.starter.wechat.dto.menu.WeChatMenuDto;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@Mapper
public interface WeChatMenuConvert {

    WeChatMenuConvert CONVERT = Mappers.getMapper(WeChatMenuConvert.class);

    WeChatMenu convert(WeChatMenuParam in);

    WeChatMenuDto convert(WeChatMenu in);

    WeChatMenuInfo convert(WxMenu in);

    WxMenu convert(WeChatMenuInfo in);

}
