package cn.daxpay.open.platform.system.convert.mobile;

import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import cn.daxpay.open.platform.system.param.mobile.MobileAppParam;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 移动端应用配置外壳转换
///
/// 嵌套平台配置与 appConfig 密文由 [cn.daxpay.open.platform.system.service.mobile.MobileAppService] 处理,
/// 此处仅映射外壳字段。
@Mapper
public interface MobileAppConvert {

    MobileAppConvert CONVERT = Mappers.getMapper(MobileAppConvert.class);

    /// 实体转结果(嵌套配置由 Service 填充)
    @Mapping(target = "wxMini", ignore = true)
    @Mapping(target = "alipayMini", ignore = true)
    @Mapping(target = "dyMini", ignore = true)
    MobileAppResult toResult(MobileApp entity);

    /// 参数转实体(新增, appConfig 由 Service 序列化后写入)
    @Mapping(target = "appConfig", ignore = true)
    MobileApp toEntity(MobileAppParam param);

    /// 参数拷贝到实体(更新)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "appConfig", ignore = true)
    void copy(MobileAppParam param, @MappingTarget MobileApp entity);
}
