package cn.daxpay.open.channel.douyin.convert.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppCapability;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 抖音直连商户应用支付能力关联转换
///
/// MapStruct转换器，负责关联记录在实体与返回结果之间的转换。
///
@Mapper
public interface DouyinDirectAppCapabilityConvert {

    DouyinDirectAppCapabilityConvert CONVERT = Mappers.getMapper(DouyinDirectAppCapabilityConvert.class);

    /// 转换为返回对象
    DouyinDirectAppCapabilityResult toResult(DouyinDirectAppCapability entity);
}
