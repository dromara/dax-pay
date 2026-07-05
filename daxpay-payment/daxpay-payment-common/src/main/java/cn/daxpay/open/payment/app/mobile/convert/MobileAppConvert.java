package cn.daxpay.open.payment.app.mobile.convert;

import cn.daxpay.open.payment.app.mobile.entity.MobileApp;
import cn.daxpay.open.payment.app.mobile.param.MobileAppParam;
import cn.daxpay.open.payment.app.mobile.result.MobileAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 移动端应用配置转换
@Mapper
public interface MobileAppConvert {

    MobileAppConvert CONVERT = Mappers.getMapper(MobileAppConvert.class);

    /// 实体转结果
    MobileAppResult toResult(MobileApp entity);

    /// 参数转实体(新增)
    MobileApp toEntity(MobileAppParam param);

    /// 参数拷贝到实体(更新)
    void copy(MobileAppParam param, @MappingTarget MobileApp entity);
}
