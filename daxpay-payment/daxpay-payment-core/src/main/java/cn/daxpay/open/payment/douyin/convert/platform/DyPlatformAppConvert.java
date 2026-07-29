package cn.daxpay.open.payment.douyin.convert.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppParam;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 平台抖音应用转换
///
@Mapper
public interface DyPlatformAppConvert {

    DyPlatformAppConvert CONVERT = Mappers.getMapper(DyPlatformAppConvert.class);

    /// 转换为返回对象
    DyPlatformAppResult toResult(DyPlatformApp entity);

    /// 转换为实体
    DyPlatformApp toEntity(DyPlatformAppParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DyPlatformAppParam param, @MappingTarget DyPlatformApp entity);
}
