package cn.daxpay.open.platform.capability.sensitiveword.convert;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 敏感词命中转换
///
@Mapper
public interface SystemSensitiveWordHitConvert {
    SystemSensitiveWordHitConvert CONVERT = Mappers.getMapper(SystemSensitiveWordHitConvert.class);

    SystemSensitiveWordHitResult toResult(SystemSensitiveWordHit entity);
}

