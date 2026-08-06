package cn.daxpay.open.payment.merchant.convert.config;

import cn.daxpay.open.payment.merchant.entity.config.MchRiskConfig;
import cn.daxpay.open.payment.merchant.param.config.MchRiskConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchRiskConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户风控配置转换
///
/// 运营端更新时 mchNo 由 Service 显式 [MchRiskConfig#setMchNo] 写入(运营端不装载商户 PaymentContext,
/// 不能依赖 FieldFill), 故转实体时忽略 mchNo(避免 param 的 mchNo 覆盖, 统一在 Service 兜底)。
@Mapper
public interface MchRiskConfigConvert {
    MchRiskConfigConvert CONVERT = Mappers.getMapper(MchRiskConfigConvert.class);

    @Mapping(target = "mchNo", ignore = true)
    MchRiskConfig toEntity(MchRiskConfigParam param);

    MchRiskConfigResult toResult(MchRiskConfig entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "mchNo", ignore = true)
    void copy(MchRiskConfigParam param, @MappingTarget MchRiskConfig entity);
}
