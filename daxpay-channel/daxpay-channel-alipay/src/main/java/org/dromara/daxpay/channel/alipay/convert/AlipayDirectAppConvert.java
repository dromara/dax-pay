package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectApp;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectAppParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用转换
///
/// MapStruct转换器，负责支付宝直连商户应用在实体、参数和返回结果之间的相互转换。
///
@Mapper
public interface AlipayDirectAppConvert {

    AlipayDirectAppConvert CONVERT = Mappers.getMapper(AlipayDirectAppConvert.class);

    /// 转换为返回对象
    AlipayDirectAppResult toResult(AlipayDirectApp entity);

    /// 转换为实体
    AlipayDirectApp toEntity(AlipayDirectAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(AlipayDirectAppParam param, @MappingTarget AlipayDirectApp entity);
}
