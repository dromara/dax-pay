package cn.daxpay.open.payment.merchant.convert.config;

import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 商户应用事件通知配置转换
///
@Mapper
public interface MchAppNotifyConfigConvert {
    MchAppNotifyConfigConvert CONVERT = Mappers.getMapper(MchAppNotifyConfigConvert.class);

    MchAppNotifyConfig toEntity(MchAppNotifyConfigParam param);

    MchAppNotifyConfigResult toResult(MchAppNotifyConfig entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MchAppNotifyConfigParam param, @MappingTarget MchAppNotifyConfig entity);
}
