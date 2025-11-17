package org.dromara.daxpay.payment.merchant.convert.onboarded;

import cn.hutool.core.bean.copier.CopyOptions;
import org.dromara.daxpay.payment.merchant.entity.onboarded.OnbMchInfo;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoParam;
import org.dromara.daxpay.payment.merchant.result.onboarded.OnbMchInfoResult;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 进件商户信息转换
 * @author xxm
 * @since 2025/11/11
 */
@Mapper
public interface OnbMchInfoConvert {
    OnbMchInfoConvert CONVERT = Mappers.getMapper(OnbMchInfoConvert.class);

    OnbMchInfoResult toResult(OnbMchInfo entity);

    OnbMchInfo toEntity(OnbMchInfoParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(OnbMchInfoParam param, @MappingTarget OnbMchInfo onbMchInfo);
}
