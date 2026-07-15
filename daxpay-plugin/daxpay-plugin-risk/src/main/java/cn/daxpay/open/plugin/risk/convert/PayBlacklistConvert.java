package cn.daxpay.open.plugin.risk.convert;

import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.param.PayBlacklistParam;
import cn.daxpay.open.plugin.risk.result.PayBlacklistResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 黑名单转换
///
@Mapper
public interface PayBlacklistConvert {
    PayBlacklistConvert CONVERT = Mappers.getMapper(PayBlacklistConvert.class);

    PayBlacklistResult toResult(PayBlacklist entity);

    PayBlacklist toEntity(PayBlacklistParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PayBlacklistParam param, @MappingTarget PayBlacklist entity);
}
