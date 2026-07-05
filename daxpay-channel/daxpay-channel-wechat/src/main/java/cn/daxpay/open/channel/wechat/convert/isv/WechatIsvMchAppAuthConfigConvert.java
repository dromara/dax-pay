package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppAuthConfigParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信服务商通道商户应用授权认证配置转换
///
/// MapStruct转换器,负责微信服务商通道商户应用授权认证配置在实体、参数和返回结果之间的转换,更新时空值不覆盖。
///
@Mapper
public interface WechatIsvMchAppAuthConfigConvert {

    WechatIsvMchAppAuthConfigConvert CONVERT = Mappers.getMapper(WechatIsvMchAppAuthConfigConvert.class);

    /// 转换为返回对象
    WechatIsvMchAppAuthConfigResult toResult(WechatIsvMchAppAuthConfig entity);

    /// 转换为实体
    WechatIsvMchAppAuthConfig toEntity(WechatIsvMchAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatIsvMchAppAuthConfigParam param, @MappingTarget WechatIsvMchAppAuthConfig entity);
}
