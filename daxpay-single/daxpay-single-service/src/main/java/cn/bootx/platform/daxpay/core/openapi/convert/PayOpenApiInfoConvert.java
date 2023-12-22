package cn.bootx.platform.daxpay.core.openapi.convert;

import cn.bootx.platform.daxpay.core.openapi.entity.PayOpenApiInfo;
import cn.bootx.platform.daxpay.param.openapi.PayOpenApiInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 开放接口信息转换
 * @author xxm
 * @since 2023/12/22
 */
@Mapper
public interface PayOpenApiInfoConvert {
    PayOpenApiInfoConvert CONVERT = Mappers.getMapper(PayOpenApiInfoConvert.class);

    PayOpenApiInfo convert(PayOpenApiInfoParam in);
}
