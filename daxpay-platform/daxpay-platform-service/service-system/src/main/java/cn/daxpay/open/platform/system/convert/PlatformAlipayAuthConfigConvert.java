package cn.daxpay.open.platform.system.convert;

import cn.daxpay.open.platform.system.entity.config.platform.PlatformAlipayAuthConfig;
import cn.daxpay.open.platform.system.param.config.PlatformAlipayAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformAlipayAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台支付宝开放平台认证配置转换
///
/// copy 使用 IGNORE 策略, 前端未传的敏感字段(null)不会覆盖数据库原值,
/// 与"前端 diffForm + 后端 NOT_NULL"双重保护配合, 避免误清空。
///
@Mapper
public interface PlatformAlipayAuthConfigConvert {

    PlatformAlipayAuthConfigConvert CONVERT = Mappers.getMapper(PlatformAlipayAuthConfigConvert.class);

    PlatformAlipayAuthConfigResult toResult(PlatformAlipayAuthConfig data);

    PlatformAlipayAuthConfig convert(PlatformAlipayAuthConfigParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PlatformAlipayAuthConfigParam param, @MappingTarget PlatformAlipayAuthConfig data);
}
