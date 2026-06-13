package org.dromara.daxpay.channel.wechat.convert;

import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvKeyConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvKeyConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvKeyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信服务商密钥配置转换
///
@Mapper
public interface WechatIsvKeyConfigConvert {

    WechatIsvKeyConfigConvert CONVERT = Mappers.getMapper(WechatIsvKeyConfigConvert.class);

    /// 转换为返回对象
    WechatIsvKeyConfigResult toResult(WechatIsvKeyConfig entity);

    /// 转换为实体
    WechatIsvKeyConfig toEntity(WechatIsvKeyConfigParam param);

    /// 更新源数据到实体(空值不覆盖,密钥/证书为空时保留原值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatIsvKeyConfigParam param, @MappingTarget WechatIsvKeyConfig entity);
}
