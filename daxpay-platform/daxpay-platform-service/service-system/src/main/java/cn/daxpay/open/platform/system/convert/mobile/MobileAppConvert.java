package cn.daxpay.open.platform.system.convert.mobile;

import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import cn.daxpay.open.platform.system.param.mobile.MobileAppParam;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 移动端应用配置转换
///
/// copy 使用 IGNORE 策略, 前端未传的敏感字段(null)不会覆盖数据库原值,
/// 与前端 diffForm + 后端脱敏回显双重保护配合, 避免误清空。
@Mapper
public interface MobileAppConvert {

    MobileAppConvert CONVERT = Mappers.getMapper(MobileAppConvert.class);

    /// 实体转结果
    MobileAppResult toResult(MobileApp entity);

    /// 参数转实体(新增)
    MobileApp toEntity(MobileAppParam param);

    /// 参数拷贝到实体(更新)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MobileAppParam param, @MappingTarget MobileApp entity);
}
