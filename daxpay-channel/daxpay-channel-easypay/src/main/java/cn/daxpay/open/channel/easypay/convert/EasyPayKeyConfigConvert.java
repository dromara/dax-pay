package cn.daxpay.open.channel.easypay.convert;

import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
import cn.daxpay.open.channel.easypay.param.EasyPayKeyConfigParam;
import cn.daxpay.open.channel.easypay.result.EasyPayKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 易支付通道密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EasyPayKeyConfigConvert {
    EasyPayKeyConfigConvert CONVERT = Mappers.getMapper(EasyPayKeyConfigConvert.class);

    EasyPayKeyConfigResult toResult(EasyPayKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    void copy(EasyPayKeyConfigParam param, @MappingTarget EasyPayKeyConfig entity);
}
