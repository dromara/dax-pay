package cn.daxpay.open.channel.alipay.convert.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝直连商户应用支付能力关联转换
///
/// MapStruct转换器，负责关联记录在实体与返回结果之间的转换。
///
@Mapper
public interface AlipayDirectAppCapabilityConvert {

    AlipayDirectAppCapabilityConvert CONVERT = Mappers.getMapper(AlipayDirectAppCapabilityConvert.class);

    /// 转换为返回对象
    AlipayDirectAppCapabilityResult toResult(AlipayDirectAppCapability entity);
}
