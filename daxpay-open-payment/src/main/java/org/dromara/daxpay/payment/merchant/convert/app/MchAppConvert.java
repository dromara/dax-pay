package org.dromara.daxpay.payment.merchant.convert.app;

import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.param.app.MchAppParam;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户应用转换
 * @author xxm
 * @since 2024/6/24
 */
@Mapper
public interface MchAppConvert {
    MchAppConvert CONVERT = Mappers.getMapper(MchAppConvert.class);

    MchAppResult toResult(MchApp entity);

    MchApp toEntity(MchAppParam param);
}
