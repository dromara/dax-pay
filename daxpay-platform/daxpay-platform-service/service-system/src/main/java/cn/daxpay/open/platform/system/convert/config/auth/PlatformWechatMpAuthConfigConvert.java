package cn.daxpay.open.platform.system.convert.config.auth;

import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.param.config.auth.PlatformWechatMpAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformWechatMpAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台微信公众号 H5 认证配置转换
///
/// copy 使用 IGNORE 策略, 前端未传的敏感字段(null)不会覆盖数据库原值,
/// 与"前端 diffForm + 后端 NOT_NULL"双重保护配合, 避免误清空。
///
@Mapper
public interface PlatformWechatMpAuthConfigConvert {

    PlatformWechatMpAuthConfigConvert CONVERT = Mappers.getMapper(PlatformWechatMpAuthConfigConvert.class);

    PlatformWechatMpAuthConfigResult toResult(PlatformWechatMpAuthConfig data);

    PlatformWechatMpAuthConfig convert(PlatformWechatMpAuthConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformWechatMpAuthConfigParam param, @MappingTarget PlatformWechatMpAuthConfig data);
}
