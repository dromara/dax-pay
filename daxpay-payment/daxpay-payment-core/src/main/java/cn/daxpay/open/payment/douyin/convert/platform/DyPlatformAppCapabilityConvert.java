package cn.daxpay.open.payment.douyin.convert.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppCapability;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppCapabilityParam;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 平台抖音应用默认能力绑定转换
///
@Mapper
public interface DyPlatformAppCapabilityConvert {

    DyPlatformAppCapabilityConvert CONVERT = Mappers.getMapper(DyPlatformAppCapabilityConvert.class);

    /// 转换为返回对象
    DyPlatformAppCapabilityResult toResult(DyPlatformAppCapability entity);

    /// 转换为实体
    DyPlatformAppCapability toEntity(DyPlatformAppCapabilityParam param);
}
