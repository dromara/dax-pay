package cn.daxpay.open.payment.douyin.convert.channel;

import cn.daxpay.open.payment.douyin.entity.channel.DyChannelAppCapability;
import cn.daxpay.open.payment.douyin.param.channel.DyChannelAppCapabilityParam;
import cn.daxpay.open.payment.douyin.result.channel.DyChannelAppCapabilityResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 通道商户抖音应用能力绑定转换
///
@Mapper
public interface DyChannelAppCapabilityConvert {

    DyChannelAppCapabilityConvert CONVERT = Mappers.getMapper(DyChannelAppCapabilityConvert.class);

    /// 转换为返回对象
    DyChannelAppCapabilityResult toResult(DyChannelAppCapability entity);

    /// 转换为实体
    DyChannelAppCapability toEntity(DyChannelAppCapabilityParam param);
}
