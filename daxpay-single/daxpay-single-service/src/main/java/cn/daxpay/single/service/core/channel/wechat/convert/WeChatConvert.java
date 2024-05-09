package cn.daxpay.single.service.core.channel.wechat.convert;

import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.dto.channel.wechat.WeChatPayConfigDto;
import cn.daxpay.single.service.param.channel.wechat.WeChatPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信转换类
 *
 * @author xxm
 * @since 2021/6/21
 */
@Mapper
public interface WeChatConvert {

    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigParam in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

}
