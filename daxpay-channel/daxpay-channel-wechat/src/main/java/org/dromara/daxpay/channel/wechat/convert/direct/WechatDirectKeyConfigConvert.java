package org.dromara.daxpay.channel.wechat.convert.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectKeyConfig;
import org.dromara.daxpay.channel.wechat.param.direct.WechatDirectKeyConfigParam;
import org.dromara.daxpay.channel.wechat.result.direct.WechatDirectKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信直连密钥配置转换
///
/// MapStruct转换器，负责微信直连密钥配置在实体、参数和返回结果之间的转换，更新时空值不覆盖。
///
@Mapper
public interface WechatDirectKeyConfigConvert {

    WechatDirectKeyConfigConvert CONVERT = Mappers.getMapper(WechatDirectKeyConfigConvert.class);

    /// 转换为返回对象
    WechatDirectKeyConfigResult toResult(WechatDirectKeyConfig entity);

    /// 转换为实体
    WechatDirectKeyConfig toEntity(WechatDirectKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖,密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatDirectKeyConfigParam param, @MappingTarget WechatDirectKeyConfig entity);
}
