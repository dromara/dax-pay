package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfigEntity;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.param.config.WechatPaySubConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WechatPayConfigConvert {
    WechatPayConfigConvert CONVERT = Mappers.getMapper(WechatPayConfigConvert.class);

    WechatPayConfigResult toResult(WechatPayConfigEntity in);

    WechatPayConfigEntity copy(WechatPayConfigEntity in);

    WechatPayConfigEntity toEntity(WechatPayConfigParam in);

    void copy(WechatPayConfigParam param, @MappingTarget WechatPayConfigEntity subConfig);

    void copy(WechatPaySubConfigParam param, @MappingTarget WechatPaySubConfig subConfig);

    WechatPayConfig toConfig(WechatPayConfigEntity payConfig);
}
