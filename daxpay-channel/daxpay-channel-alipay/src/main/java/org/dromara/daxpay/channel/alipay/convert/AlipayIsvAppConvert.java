package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.app.AlipayIsvApp;
import org.dromara.daxpay.channel.alipay.param.app.AlipayIsvAppParam;
import org.dromara.daxpay.channel.alipay.result.app.AlipayIsvAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商应用转换
///
@Mapper
public interface AlipayIsvAppConvert {

    AlipayIsvAppConvert CONVERT = Mappers.getMapper(AlipayIsvAppConvert.class);

    /// 转换为返回对象
    AlipayIsvAppResult toResult(AlipayIsvApp entity);

    /// 转换为实体
    AlipayIsvApp toEntity(AlipayIsvAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(AlipayIsvAppParam param, @MappingTarget AlipayIsvApp entity);
}
