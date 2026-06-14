package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.app.AlipayMchApp;
import org.dromara.daxpay.channel.alipay.param.app.AlipayMchAppParam;
import org.dromara.daxpay.channel.alipay.result.app.AlipayMchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用转换
///
@Mapper
public interface AlipayMchAppConvert {

    AlipayMchAppConvert CONVERT = Mappers.getMapper(AlipayMchAppConvert.class);

    /// 转换为返回对象
    AlipayMchAppResult toResult(AlipayMchApp entity);

    /// 转换为实体
    AlipayMchApp toEntity(AlipayMchAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(AlipayMchAppParam param, @MappingTarget AlipayMchApp entity);
}
