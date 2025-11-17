package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatPaySubConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPaySubConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/2/11
 */
@Mapper
public interface WechatPaySubConfigConvert {
    WechatPaySubConfigConvert CONVERT = Mappers.getMapper(WechatPaySubConfigConvert.class);

    WechatPaySubConfig toEntity(WechatPaySubConfigParam in);

    WechatPaySubConfig copy(WechatPaySubConfig in);

    WechatPaySubConfigResult toResult(WechatPaySubConfig in);
}
