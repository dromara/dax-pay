package cn.daxpay.open.channel.alipay.convert.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig;
import cn.daxpay.open.channel.alipay.param.direct.AlipayTransferConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 支付宝转账配置转换
///
/// MapStruct 转换器, 负责转账配置在实体、参数和返回结果之间的转换, 更新时空值不覆盖。
/// 冗余展示字段(转出应用名/aliAppId/应用类型)由 Service 填充, 不经 Convert。
///
@Mapper
public interface AlipayTransferConfigConvert {

    AlipayTransferConfigConvert CONVERT = Mappers.getMapper(AlipayTransferConfigConvert.class);

    /// 转换为返回对象
    AlipayTransferConfigResult toResult(AlipayTransferConfig entity);

    /// 转换为实体
    AlipayTransferConfig toEntity(AlipayTransferConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(AlipayTransferConfigParam param, @MappingTarget AlipayTransferConfig entity);
}
