package cn.daxpay.open.channel.alipay.convert.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferSceneConfig;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝转账场景配置转换
///
/// MapStruct 转换器,负责转账场景配置在实体与返回结果之间的转换。
///
@Mapper
public interface AlipayTransferSceneConfigConvert {

    AlipayTransferSceneConfigConvert CONVERT = Mappers.getMapper(AlipayTransferSceneConfigConvert.class);

    /// 转换为返回对象
    AlipayTransferSceneConfigResult toResult(AlipayTransferSceneConfig entity);
}
