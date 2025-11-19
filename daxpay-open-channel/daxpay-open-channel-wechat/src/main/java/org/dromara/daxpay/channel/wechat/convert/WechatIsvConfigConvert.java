package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/1
 */
@Mapper
public interface WechatIsvConfigConvert {
    WechatIsvConfigConvert CONVERT = Mappers.getMapper(WechatIsvConfigConvert.class);

    WechatIsvConfigResult toResult(WechatIsvConfig wechatIsvConfig);

    WechatIsvConfig toEntity(WechatIsvConfigParam wechatIsvConfigParam);

    WechatIsvConfig copy(WechatIsvConfig wechatIsvConfig);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void copy(WechatIsvConfigParam param, @MappingTarget WechatIsvConfig wechatIsvConfig);
}
