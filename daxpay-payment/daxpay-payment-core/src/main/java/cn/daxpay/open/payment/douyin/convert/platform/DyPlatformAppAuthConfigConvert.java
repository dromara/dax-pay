package cn.daxpay.open.payment.douyin.convert.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppAuthConfig;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppAuthConfigParam;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppAuthConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台抖音应用授权认证配置转换
///
@Mapper
public interface DyPlatformAppAuthConfigConvert {

    DyPlatformAppAuthConfigConvert CONVERT = Mappers.getMapper(DyPlatformAppAuthConfigConvert.class);

    /// 转换为返回对象
    DyPlatformAppAuthConfigResult toResult(DyPlatformAppAuthConfig entity);

    /// 转换为实体
    DyPlatformAppAuthConfig toEntity(DyPlatformAppAuthConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DyPlatformAppAuthConfigParam param, @MappingTarget DyPlatformAppAuthConfig entity);
}
