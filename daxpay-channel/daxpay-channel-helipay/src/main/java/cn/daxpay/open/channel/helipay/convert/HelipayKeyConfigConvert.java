package cn.daxpay.open.channel.helipay.convert;

import cn.daxpay.open.channel.helipay.entity.HelipayKeyConfig;
import cn.daxpay.open.channel.helipay.param.HelipayKeyConfigParam;
import cn.daxpay.open.channel.helipay.result.HelipayKeyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 合利宝通道密钥配置转换
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HelipayKeyConfigConvert {
    HelipayKeyConfigConvert CONVERT = Mappers.getMapper(HelipayKeyConfigConvert.class);

    HelipayKeyConfigResult toResult(HelipayKeyConfig entity);

    /// 更新源数据到实体(空值不覆盖, 密钥为空时保留原值)
    void copy(HelipayKeyConfigParam param, @MappingTarget HelipayKeyConfig entity);
}
