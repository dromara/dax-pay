package cn.daxpay.open.channel.wechat.convert.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppAuthConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信直连商户应用授权认证配置转换
///
/// MapStruct转换器，负责微信直连商户应用授权认证配置在实体、参数和返回结果之间的转换，更新时空值不覆盖。
///
@Mapper
public interface WechatDirectAppAuthConfigConvert {

    WechatDirectAppAuthConfigConvert CONVERT = Mappers.getMapper(WechatDirectAppAuthConfigConvert.class);

    /// 转换为返回对象
    WechatDirectAppAuthConfigResult toResult(WechatDirectAppAuthConfig entity);

    /// 转换为实体
    WechatDirectAppAuthConfig toEntity(WechatDirectAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatDirectAppAuthConfigParam param, @MappingTarget WechatDirectAppAuthConfig entity);
}
