package org.dromara.daxpay.channel.wechat.convert.isv;

import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信服务商应用授权认证配置转换
///
@Mapper
public interface WechatIsvAppAuthConfigConvert {

    WechatIsvAppAuthConfigConvert CONVERT = Mappers.getMapper(WechatIsvAppAuthConfigConvert.class);

    /// 转换为返回对象
    WechatIsvAppAuthConfigResult toResult(WechatIsvAppAuthConfig entity);

    /// 转换为实体
    WechatIsvAppAuthConfig toEntity(WechatIsvAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatIsvAppAuthConfigParam param, @MappingTarget WechatIsvAppAuthConfig entity);
}
