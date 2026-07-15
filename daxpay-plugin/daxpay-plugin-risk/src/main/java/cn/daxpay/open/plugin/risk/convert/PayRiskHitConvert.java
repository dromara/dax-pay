package cn.daxpay.open.plugin.risk.convert;

import cn.daxpay.open.plugin.risk.entity.PayRiskHit;
import cn.daxpay.open.plugin.risk.result.PayRiskHitResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 风险命中转换
///
@Mapper
public interface PayRiskHitConvert {
    PayRiskHitConvert CONVERT = Mappers.getMapper(PayRiskHitConvert.class);

    PayRiskHitResult toResult(PayRiskHit entity);
}
